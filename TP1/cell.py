class Cell:
    def __init__(self,row, col,id,M,L):
        self.id=id
        self.row = row
        self.col = col
        self.list = []; #Particles in cell
        self.isRightSide = True if col == (L/M - 1) else False
        self.isBottomSide = True if row == (L/M - 1) else False

        
    def add_particle(self, particle):
        if(self.list.__len__ == 0):
            self.heads = particle
        self.list.append(particle)
    
    def get_particles(self):
        return self.list
    
    def get_head(self):
        return self.head
    
    def is_cell_right_side(self):
        return self.isRightSide
    
    def is_cell_bottom_side(self):
        return self.isBottomSide

    def __str__(self):
        return (str(particle) for particle in self.list)