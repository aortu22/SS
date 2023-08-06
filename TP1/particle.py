class Particle:
    def __init__(self, id):
        self.id = id;
        self.neighbours = [];
    
    def add_neighbour(self, particle):
        self.neighbours.append(particle)
        
    def __str__(self):
        return self.id
    
    def get_neighbours(self):
        return self.neighbours