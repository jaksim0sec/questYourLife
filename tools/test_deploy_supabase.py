"""Deployment preflight must reject a different project before using its credentials."""
import unittest
from deploy_supabase import project_url, forward_sql
from pathlib import Path

class DeploymentProjectTest(unittest.TestCase):
    def test_only_authorized_project_can_receive_management_requests(self):
        self.assertEqual('https://api.supabase.com/v1/projects/xpqjjfcyiirospxzykqd', project_url('xpqjjfcyiirospxzykqd'))
        with self.assertRaises(ValueError): project_url('unrelated-project')
        with self.assertRaises(ValueError): project_url('../other')

    def test_forward_migration_is_locked_and_rejects_history_drift(self):
        for path in sorted((Path(__file__).resolve().parents[1]/'supabase/migrations').glob('*.sql')):
            if path.name.startswith('202611030001_'): continue
            sql=forward_sql(path)
            self.assertIn('pg_advisory_xact_lock', sql)
            self.assertIn('Applied migration differs', sql)
            self.assertIn(':sha256:', sql)
            self.assertIn('supabase_migrations.schema_migrations', sql)

if __name__ == '__main__': unittest.main()
