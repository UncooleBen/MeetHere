# locust -f mixed1.py --host=http://localhost:8080/MeetHere_war/ -t30m --no-web --csv=mixed1 -c 200 -r 1
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
        username = 'username00'
        password = 'password000'
        self.client.post("/login",
            {
                'username' : username,
                'password' : password,
            })

    @task
    def news(self):
        self.client.get("/news?action=list")

class WebsiteUser(HttpLocust):
    task_set = WebsiteTasks
    wait_time = between(5, 15)
