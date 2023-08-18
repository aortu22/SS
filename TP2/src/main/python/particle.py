class Particle:
    def __init__(self, id):
    # def __init__(self, id, r):
        self.id = id
        # self.r = r
        self.x = 0
        self.y = 0
        self.neighbours = []

    def set_postion(self, x, y):
        self.x = x
        self.y = y

    def add_neighbours(self, neighbours):
        self.neighbours = neighbours
