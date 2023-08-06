class Cell:
    def __init__(self,row, col):
        self.row = row
        self.col = col
        self.list = []; #Particles in cell
        
    def add_particle(self, particle):
        if(self.list.__len__ == 0):
            self.heads = particle
        self.list.append(particle)
    
    def get_particles(self):
        return self.list
    
    def get_head(self):
        return self.head
    
    def __str__(self):
        return (str(particle) for particle in self.list)