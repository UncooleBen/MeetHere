from locust import HttpLocust, TaskSet, task, between

class WebsiteTasks(TaskSet):
    def on_start(self):
        self.client.post("/login", {
            "username": "user",
            "password": "user"
        })
    
    @task
    def showNewsDetail(self):
        self.client.get("/news?action=detail&newsId=948")

class WebsiteUser(HttpLocust):
    task_set = WebsiteTasks
    wait_time = between(5, 15)
