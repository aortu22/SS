
class Particle:
    def __init__(self, id, x, y, r):
        self.id = id
        self.x = x
        self.y = y
        self.r = r

    def add_neighbours(self,neighbours):
        self.neighbours = neighbours
