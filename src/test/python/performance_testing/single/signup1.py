from locust import HttpLocust, TaskSet, task, between
import random

class WebsiteTasks(TaskSet):

    lowercase = ['a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm'
    , 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z']

    username = ''.join(random.choice(self.lowercase) for x in range(10))
    password = ''.join(random.choice(self.lowercase) for x in range(15))
    name =  ''.join(random.choice(self.lowercase) for x in range(5))
    sex = 'FEMALE'
    tel = '13000000000'

    def on_start(self):
        self.client.post("/signupSubmit",
            {
                'username' : username,
                'password' : password,
                'name' : name,
                'sex' : sex,
                'tel' : tel
            })

    @task
    def login(self):
        self.client.post("/login",
        {
            'username' : username,
            'password' : password,
        })

    @task
    def logout(self):
        self.client.get("/index")



class WebsiteUser(HttpLocust):
    task_set = WebsiteTasks
    wait_time = between(5, 15)
