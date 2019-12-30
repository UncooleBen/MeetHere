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
        self.client.get('/signup')

    @task
    def signup(self):
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

class WebsiteUser(HttpLocust):
    task_set = WebsiteTasks
    wait_time = between(5, 15)
