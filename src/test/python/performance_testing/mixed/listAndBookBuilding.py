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
        self.client.get("/building?action=list")
        self.client.post("/building?action=book",{
            "buildingId":"632",
            "startDate":"2020-01-02",
            "duration":"2"
        })
        self.client.get("/index")

class WebsiteUser(HttpLocust):
    task_set = WebsiteTasks
    wait_time = between(5, 15)
