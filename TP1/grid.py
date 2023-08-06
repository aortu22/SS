from cell import Cell


class Grid:
    def __init__(self, rows, columns):
        self.rows = rows
        self.columns = columns
        self.grid = [[Cell(row, col) for col in range(columns)] for row in range(rows)]
    
    def get_cell(self, row, column):
        return self.grid[row][column]
    
    def add_to_cell(self, row, column, particle):
        self.grid[row][column].add_particle(particle)
    
    def get_particles(self, x, y):
        return [particle for particle in self.grid[x][y].get_particles()]