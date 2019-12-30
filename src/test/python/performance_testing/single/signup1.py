from locust import HttpLocust, TaskSet, task, between
import random

class WebsiteTasks(TaskSet):

    lowercase = ['a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm'
    , 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z']

    def on_start(self):
        self.client.post("/signup")

    @task
    def signup(self):
        username = ''.join(random.choice(self.lowercase) for x in range(10))
        password = ''.join(random.choice(self.lowercase) for x in range(15))
        name =  ''.join(random.choice(self.lowercase) for x in range(5))
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
