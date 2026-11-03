package com.questyourlife.app.ui

import android.app.Application
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.questyourlife.app.data.*
import com.questyourlife.app.daily.*
import com.questyourlife.app.companion.*
import com.questyourlife.app.cosmetics.*
import com.questyourlife.app.sync.*
import androidx.room.withTransaction
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

data class QuestReward(val title: String,val amount:Int,val isGoal:Boolean,val nonce:Long=System.nanoTime())

@OptIn(ExperimentalCoroutinesApi::class)
class DailyQuestViewModel(application: Application) : AndroidViewModel(application) {
    private val app=application.applicationContext
    private val profileFile=ProfileRegistry.get(app).file.value
    private val daily=DailyQuestRepository(app)
    private val goalRepo=GoalRepository(app)
    private val day=MutableStateFlow(DailyQuestRepository.today())

    val quests=day.flatMapLatest(daily::observeDay).stateIn(viewModelScope,SharingStarted.WhileSubscribed(5000),emptyList())
    val history=daily.observeHistory().stateIn(viewModelScope,SharingStarted.WhileSubscribed(5000),emptyList<DailyQuestEntity>())
    val dailyCash=daily.observeCash().stateIn(viewModelScope,SharingStarted.WhileSubscribed(5000),0)
    val completedCount=daily.observeCompletionCount().stateIn(viewModelScope,SharingStarted.WhileSubscribed(5000),0)
    val goals=goalRepo.watchGoals().stateIn(viewModelScope,SharingStarted.WhileSubscribed(5000),emptyList<GoalEntity>())
    val steps=goalRepo.watchSteps().stateIn(viewModelScope,SharingStarted.WhileSubscribed(5000),emptyList<GoalStepEntity>())
    val ownedItems=goalRepo.watchItems().stateIn(viewModelScope,SharingStarted.WhileSubscribed(5000),emptyList<OwnedItemEntity>())
    val sessions=goalRepo.watchSessions().stateIn(viewModelScope,SharingStarted.WhileSubscribed(5000),emptyList<FocusSessionEntity>())
    private val syncDb=QuestDatabase.get(app)
    val syncMeta=syncDb.syncDao().observeMeta().stateIn(viewModelScope,SharingStarted.WhileSubscribed(5000),null)
    val pendingCash=syncDb.syncDao().observePendingCash().stateIn(viewModelScope,SharingStarted.WhileSubscribed(5000),0L)
    val appearance=syncMeta.map { it?.appearance ?: "system" }.distinctUntilChanged().stateIn(viewModelScope,SharingStarted.WhileSubscribed(5000),"system")
    val userLevel=combine(syncMeta,history,steps) { meta,allDaily,mainSteps ->
        com.questyourlife.app.progression.UserLevel.fromXp(if(meta?.userId!=null) meta.confirmedXp else
            allDaily.sumOf { if(it.completedAt!=null)it.reward.toLong() else 0L }+mainSteps.sumOf { if(it.completedAt!=null)it.reward.toLong() else 0L })
    }.stateIn(viewModelScope,SharingStarted.WhileSubscribed(5000),com.questyourlife.app.progression.UserLevel.fromXp(0))
    val cash=combine(syncMeta,pendingCash) { meta,pending ->
        (if(meta?.userId!=null)meta.confirmedCash else (meta?.legacyCash ?: 0L)+pending).coerceIn(0,Int.MAX_VALUE.toLong()).toInt()
    }.stateIn(viewModelScope,SharingStarted.WhileSubscribed(5000),0)
    fun setAppearance(value: String) { viewModelScope.launch(Dispatchers.IO) { SyncCapture.appearance(syncDb,value); SyncScheduler.enqueue(app) } }

    private val companion: CompanionGateway = LocalCompanionGateway(QuestDatabase.get(app))
    val questPreview = QuestPreviewStore(app)
    val contacts = companion.contacts()
    val demoClan = companion.demoClan()
    val nearbyClans = companion.nearbyClanRanks()
    fun clanPreview(previous: Boolean = false) = companion.clanPreview(previous)
    val activeThread = MutableStateFlow("")
    val chatMessages = activeThread.flatMapLatest(companion::watchMessages)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
    private val _chatBusy = MutableStateFlow(false)
    val chatBusy: StateFlow<Boolean> = _chatBusy.asStateFlow()
    val chatError = mutableStateOf<String?>(null)
    fun openThread(id: String) {
        activeThread.value = id
        viewModelScope.launch(Dispatchers.IO) { companion.openThread(id) }
    }
    fun sendMessage(text: String) {
        if (_chatBusy.value || text.isBlank()) return
        val thread = activeThread.value
        if (thread.isBlank()) return
        _chatBusy.value = true
        chatError.value = null
        viewModelScope.launch {
            try { withContext(Dispatchers.IO) { companion.send(thread, text) } }
            catch (e: kotlinx.coroutines.CancellationException) { throw e }
            catch (e: Exception) { chatError.value = "계획을 저장하지 못했어요. 입력을 확인하고 다시 시도해 주세요." }
            finally { _chatBusy.value = false }
        }
    }
    fun applyChatPlan(id: String) { viewModelScope.launch {
        try { withContext(Dispatchers.IO) { companion.applyPlan(id) }; SyncScheduler.enqueue(app); refresh() }
        catch (e: kotlinx.coroutines.CancellationException) { throw e }
        catch (e: Exception) { chatError.value = "계획을 추가하지 못했어요. 다시 시도해 주세요." }
    } }
    fun deferQuest(id: String) { viewModelScope.launch(Dispatchers.IO) {
        DailyQuestNotifications.serialized {
        if (daily.defer(id) == 1) DailyQuestNotifications.dismissActive(app,id,profileFile)
        }
    } }
    fun rejectQuest(id: String) { viewModelScope.launch(Dispatchers.IO) {
        DailyQuestNotifications.serialized {
        if (daily.reject(id) == 1) DailyQuestNotifications.dismissActive(app,id,profileFile)
        }
    } }
    fun abandonQuest(id: String) { viewModelScope.launch(Dispatchers.IO) {
        DailyQuestNotifications.serialized {
        if (daily.abandon(id) == 1) DailyQuestNotifications.dismissActive(app,id,profileFile)
        }
    } }

    val lastReward=mutableStateOf<QuestReward?>(null)
    var equipped=androidx.compose.runtime.mutableIntStateOf(0)
        private set
    var hour=androidx.compose.runtime.mutableIntStateOf(DailyQuestScheduler.hour(app))
        private set
    var minute=androidx.compose.runtime.mutableIntStateOf(DailyQuestScheduler.minute(app))
        private set
    var reminders=androidx.compose.runtime.mutableStateOf(DailyQuestScheduler.enabled(app))
        private set

    init {
        DailyQuestNotifications.setup(app)
        DailyQuestScheduler.scheduleNext(app)
        viewModelScope.launch(Dispatchers.IO) {
            syncDb.withTransaction {
                syncDb.syncDao().ensureMeta()
                val meta=syncDb.syncDao().meta()!!
                if(!meta.prefsImported) {
                    val original=ProfileRegistry.get(app).file.value=="quests.db"
                    val mode=if(original) app.getSharedPreferences("patch_appearance",android.content.Context.MODE_PRIVATE).getString("mode","system") ?: "system" else "system"
                    val item=if(original) app.getSharedPreferences("quest_main_settings",android.content.Context.MODE_PRIVATE).getInt("equipped_hat",0) else 0
                    syncDb.syncDao().setMeta(meta.copy(appearance=mode,prefsImported=true,equipped=item))
                }
            }
            SyncScheduler.enqueue(app)
            syncDb.syncDao().observeMeta().collect { it?.let { meta -> equipped.intValue=meta.equipped } }
        }
        refresh()
        viewModelScope.launch(Dispatchers.IO) { goalRepo.seedDemoIfNew() }
    }

    fun refresh(){viewModelScope.launch(Dispatchers.IO){day.value=daily.ensureToday()}}
    fun accept(id:String){viewModelScope.launch(Dispatchers.IO){
        DailyQuestNotifications.serialized {
            daily.accept(id)?.let { DailyQuestNotifications.active(app,it,profileFile) }
        }
    }}
    fun complete(id:String){viewModelScope.launch {
        val result=withContext(Dispatchers.IO){
            DailyQuestNotifications.serialized {
                daily.complete(id)?.also { DailyQuestNotifications.reward(app,it,profileFile) }
            }
        }
        if(result!=null) lastReward.value=QuestReward(result.title,result.reward,false)
    }}
    fun addGoal(title:String){viewModelScope.launch(Dispatchers.IO){goalRepo.create(title)}}
    fun completeStep(stableId:String){viewModelScope.launch {
        val target=steps.value.firstOrNull{it.syncId==stableId && it.deletedAt==null}
        if (target != null) {
            val result = withContext(Dispatchers.IO) {
                DailyQuestNotifications.serialized {
                goalRepo.complete(stableId)?.also { completion ->
                    completion.dailyId?.let { DailyQuestNotifications.dismissActive(app,it,profileFile) }
                }
                }
            }
            if (result != null) lastReward.value = QuestReward(target.title, result.reward, true)
        }
    }}
    fun clearReward(){lastReward.value=null}
    val cosmeticBusy = mutableStateOf(false)
    val cosmeticMessage = mutableStateOf<String?>(null)
    fun chooseCosmetic(id: Int, applyAfterPurchase: Boolean = true) {
        if (cosmeticBusy.value) return
        cosmeticBusy.value = true
        cosmeticMessage.value = null
        viewModelScope.launch {
            try {
                val result = withContext(Dispatchers.IO) {
                    val owned = goalRepo.dao.currentOwned(id)
                    val meta=syncDb.syncDao().meta()
                    val balance=(if(meta?.userId!=null)meta.confirmedCash else meta?.legacyCash ?: 0L).coerceIn(0,Int.MAX_VALUE.toLong()).toInt()
                    val decision = CosmeticPolicy.decide(id, if (owned) setOf(id) else emptySet(), balance)
                    when (decision) {
                        is CosmeticDecision.Equip -> true
                        is CosmeticDecision.Buy -> {
                            if(meta?.userId!=null) {
                                goalRepo.requestPurchase(id)
                                cosmeticMessage.value="구매 확인을 요청했어요. 확정되면 보관함에 표시돼요."
                                return@withContext false
                            }
                            goalRepo.buy(id,decision.item.price) || goalRepo.dao.currentOwned(id)
                        }
                        is CosmeticDecision.Reject -> false
                    }
                }
                if (result) {
                    if (applyAfterPurchase) {
                        goalRepo.setEquipped(id)
                        equipped.intValue = id
                    }
                    cosmeticMessage.value = if (applyAfterPurchase) "적용했어요." else "구매했어요. 적용하기를 누르면 착용해요."
                } else if(cosmeticMessage.value==null) {
                    cosmeticMessage.value = if (CosmeticCatalog.find(id) == null) "보유한 아이템만 착용할 수 있어요."
                        else "캐쉬가 부족해요. 잔액을 확인해 주세요."
                }
            } catch (e: kotlinx.coroutines.CancellationException) { throw e }
            catch (e: Exception) { cosmeticMessage.value = "저장하지 못했어요. 다시 시도해 주세요." }
            finally { cosmeticBusy.value = false }
        }
    }

    fun finishFocus(seconds:Int){viewModelScope.launch(Dispatchers.IO){goalRepo.recordFocus(seconds)}}
    fun setTime(h:Int,m:Int){
        DailyQuestScheduler.setTime(app,h,m);hour.intValue=h;minute.intValue=m
    }
    fun setReminders(value:Boolean){
        DailyQuestScheduler.setEnabled(app,value);reminders.value=value
    }
}
