from locust import HttpLocust, TaskSet, task, between

class WebsiteTasks(TaskSet):
    def on_start(self):
        self.client.post("/login", {
            "username": "user",
            "password": "user"
        })

    @task
    def change_password(self):
        self.client.post("/password?action=save",{
            "userId":"2241",
            "oldPassword":"wrongOldPassword",
            "newPassword":"user001"
        })

class WebsiteUser(HttpLocust):
    task_set = WebsiteTasks
    wait_time = between(5, 15)
