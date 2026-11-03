"""Deployment preflight must reject a different project before using its credentials."""
import unittest
from deploy_supabase import project_url

class DeploymentProjectTest(unittest.TestCase):
    def test_only_authorized_project_can_receive_management_requests(self):
        self.assertEqual('https://api.supabase.com/v1/projects/xpqjjfcyiirospxzykqd', project_url('xpqjjfcyiirospxzykqd'))
        with self.assertRaises(ValueError): project_url('unrelated-project')
        with self.assertRaises(ValueError): project_url('../other')

if __name__ == '__main__': unittest.main()
