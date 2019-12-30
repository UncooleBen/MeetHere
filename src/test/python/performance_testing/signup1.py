from locust import HttpLocust, TaskSet, task, between

class WebsiteTasks(TaskSet):
    def on_start(self):
        self.client.post("/signup")

    @task
    def signup(self):
        username = ''.join(random.choice(string.lowercase) for x in range(10))
        password = ''.join(random.choice(string.lowercase) for x in range(15))
        name =  ''.join(random.choice(string.lowercase) for x in range(5))
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
