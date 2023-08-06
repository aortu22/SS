from cell import Cell
import math


class Grid:
    def __init__(self,size,length,spherical):
        self.rows = size
        self.columns = size
        self.grid = [[Cell(row, col,col+row*size,size,length) for col in range(size)] for row in range(size)]
        self.cellLength=length/size
        self.spherical=spherical
    
    def get_cell(self, row, column):
        return self.grid[row][column]
    
    def add_to_cell(self, particle):
        self.grid[math.floor(particle.y/self.cellLength)][math.floor(particle.x/self.cellLength)].add_particle(particle)
    
    def get_particles(self, x, y):
        return [particle for particle in self.grid[x][y].get_particles()]
    
    def get_neighbours(self):
        for row in range(self.rows):
            for col in range(self.columns):
                rightCell,bottomCell,bottomRightCell=self.get_neighbour_cells(row,col)
                for particle in self.grid[row][col].get_particles():
                    if(rightCell != False):
                        for particle2 in rightCell.get_particles():
                            if(particle.is_neighbour(particle2)):
                                particle.add_neighbour(particle2)
                                particle2.add_neighbour(particle)
                    if(bottomCell != False):
                        for particle2 in bottomCell.get_particles():
                            if(particle.is_neighbour(particle2)):
                                particle.add_neighbour(particle2)
                                particle2.add_neighbour(particle)
                    if(bottomRightCell != False):
                        for particle2 in bottomRightCell.get_particles():
                            if(particle.is_neighbour(particle2)):
                                particle.add_neighbour(particle2)
                                particle2.add_neighbour(particle)            

    def get_neighbour_cells(self,row,col):
        if(self.spherical):
            if(self.grid[row][col].is_cell_right_side() and self.grid[row][col].is_cell_bottom_side()):
                rightCell=self.grid[row][0]
                bottomCell=self.grid[0][col]
                bottomRightCell=self.grid[0][0]
            elif(self.grid[row][col].is_cell_right_side()):
                rightCell=self.grid[row][0]
                bottomCell=self.grid[row+1][col]
                bottomRightCell=self.grid[row+1][0] 
            elif(self.grid[row][col].is_cell_bottom_side()):
                bottomCell=self.grid[0][col]
                rightCell=self.grid[row][col+1]
                bottomRightCell=self.grid[0][col+1] 
            else:
                rightCell=self.grid[row][col+1]
                bottomCell=self.grid[row][col+1]
                bottomRightCell=self.grid[row+1][col+1] 
        else: 
            if(self.grid[row][col].is_cell_right_side() and self.grid[row][col].is_cell_bottom_side()):
                rightCell=self.grid[row][0]
                bottomCell=self.grid[0][col]
                bottomRightCell=self.grid[0][0]
            elif(self.grid[row][col].is_cell_right_side()):
                rightCell=self.grid[row][0]
                bottomCell=self.grid[row+1][col]
                bottomRightCell=self.grid[row+1][0] 
            elif(self.grid[row][col].is_cell_bottom_side()):
                bottomCell=self.grid[0][col]
                rightCell=self.grid[row][col+1]
                bottomRightCell=self.grid[0][col+1] 
            else:
                rightCell=self.grid[row][col+1]
                bottomCell=self.grid[row][col+1]
                bottomRightCell=self.grid[row+1][col+1]
        return rightCell,bottomCell,bottomRightCell
         

