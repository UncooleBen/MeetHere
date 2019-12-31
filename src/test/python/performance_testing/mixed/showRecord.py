from locust import HttpLocust, TaskSet, task, between

class WebsiteTasks(TaskSet):
    def on_start(self):
        pass
    
    @task
    def showNewsDetail(self):
        self.client.post("/login", {
            "username": "user",
            "password": "user"
        })
        self.client.get("/record?action=list")
        self.client.get("/index")

class WebsiteUser(HttpLocust):
    task_set = WebsiteTasks
    wait_time = between(5, 15)
