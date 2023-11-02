input_file = "./simulation_output.txt"
output_file = "output2c.xyz"

current_frame = None

with open(input_file, 'r') as f:
    lines = f.readlines()
current_frame=0
with open(output_file, 'w') as f:
    for line in lines:
        parts = line.strip().split('\t')
        frame = int(parts[0])
        x = float(parts[1])
        y = float(parts[2])
        particle_id = int(parts[3])
        if(current_frame!=frame):
            f.write("26" + '\n')
            f.write(f"Frame {frame}\n")
            current_frame=frame

        f.write(f"{particle_id} {x} {y}\n")

