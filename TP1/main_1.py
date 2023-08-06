import json
import random
from TP1.grid import Grid
from TP1.particle import Particle


def read_and_load_json_data():
    with open('./config.json', 'r') as config_file:
        data_from_json = json.load(config_file)
        config_file.close()

    L = float(data_from_json["L"])
    M = float(data_from_json["M"])
    N = float(data_from_json["N"])
    R = float(data_from_json["R"])
    Rc = int(data_from_json["Rc"])

    return L,M,N,R,Rc

def main(): 
    L,M,N,R,Rc = read_and_load_json_data()
    grid = Grid(M,L)
    for i in range(N):
        x = random.uniform(0, L)
        y = random.uniform(0, L)
        grid.add_to_cell(Particle(i,x,y,R))
    
    

if __name__ == "__main__":
    main()