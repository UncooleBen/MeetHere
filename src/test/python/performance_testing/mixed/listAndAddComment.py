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
        self.client.get("/comment?action=list")
        self.client.get("/comment?action=add")
        self.client.post("/comment?action=save",{
            "content":"for performance test"
        })
        self.client.get("/index")

class WebsiteUser(HttpLocust):
    task_set = WebsiteTasks
    wait_time = between(5, 15)
