import math
from typing import Type

class Particle:
    def __init__(self, id,x,y,radio):
        self.id = id
        self.neighbours = []
        self.x = x
        self.y = y
        self.radio = radio
    
    def add_neighbour(self, particle):
        self.neighbours.append(particle)
        
    def __str__(self):
        return self.id
    
    def get_neighbours(self):
        return self.neighbours
    
    def get_coordenates(self):
        return self.x, self.y
    
    def is_neighbour(self,other_particle: Type[Particle],Rc):
        x1,y1 = other_particle.get_coordenates()
        return (math.sqrt((self.x - x1)**2 + (self.y - y1)**2) - self.radio*2) <= Rc
    
    #The same function to calculate the distance but taking into account circular grid
    def is_neighbour(self,other_particle: Type[Particle],Rc,correction_X,correction_y):
        x1,y1 = other_particle.get_coordenates()
        return (math.sqrt((self.x - (x1 - correction_X))**2 + (self.y - (y1 - correction_y))**2) - self.radio*2) <= Rc