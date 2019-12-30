from locust import HttpLocust, TaskSet, task, between
import random

class WebsiteTasks(TaskSet):

    def setup(self):
        self.client.get("/signup")
        username = 'username00'
        password = 'password000'
        name =  'name'
        sex = 'FEMALE'
        tel = '13000000000'
        self.client.post("/signupSubmit",
            {
                'username' : username,
                'password' : password,
                'name' : name,
                'sex' : sex,
                'tel' : tel
            })

    def on_start(self):
        pass

    @task
    def login(self):
        username = 'username00'
        password = 'password000'
        self.client.post("/login",
            {
                'username' : username,
                'password' : password,
            })
        self.client.get("/index")

class WebsiteUser(HttpLocust):
    task_set = WebsiteTasks
    wait_time = between(5, 15)
