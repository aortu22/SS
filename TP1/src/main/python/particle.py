
class Particle:
    def __init__(self, id, r):
        self.id = id
        self.r = r

    def set_postion(self,x,y):
        self.x = x
        self.y = y

    def add_neighbours(self,neighbours):
        self.neighbours = neighbours
