import math
def read_static_data(static_filename):
    # Read and extract wall length, number of particles, and frame distance
    # Return wall_length, num_particles, frame_distance
    with open(static_filename, 'r') as static_file:
        # Read the first line to get wall length
        static_file.readline().strip()
        R = float(static_file.readline().strip())
        L_fixed = float(static_file.readline().strip())

        N = int(static_file.readline().strip())

    return R, L_fixed, N


def read_dynamic_data(dynamic_filename, num_particles):
    # Read and extract data for the first frame (time, positions, angles)
    # Return time, positions, angles
    with open(dynamic_filename, 'r') as dynamic_file:
        # Read the first line to get the time
        time = float(dynamic_file.readline().strip())
        # Read positions and angles for the first frame
        particles = []

        for _ in range(num_particles):
            line = dynamic_file.readline().strip()
            x, y,vx,vy = map(float, line.split())
            particles.append((x, y))

    return time,particles


def read_output_data(output_filename):
    # Read and extract data for each frame (time, particle data)
    # Return a list of (time, particle_data) tuples
    output_data = []

    with open(output_filename, 'r') as output_file:
        lines = output_file.readlines()

        i = 0
        while i < len(lines):
            time = float(lines[i].strip())
            i += 1

            frame_particles = []
            while i < len(lines) and lines[i].strip() != "":
                particle_data = lines[i].strip().split()
                x, v = map(float, particle_data)
                frame_particles.append((x, v))
                i += 1

            output_data.append((time, frame_particles))

            i += 1  # Skip the empty line between frames

    return output_data


def create_xyz_file(output_xyz_filename, output_data,R):
    with open(output_xyz_filename, 'w') as output_xyz_file:
        # Write subsequent frames from output_data
        i=0
        for time, frame_particles in output_data:
            total_particles = len(frame_particles)+2
            output_xyz_file.write(f"{total_particles}\n")
            output_xyz_file.write(f"Frame {i} Time {time}\n")
            i+=1
            # Write dynamic particles for the frame
            output_xyz_file.write(f"1 0.0 0.01 \n")
            output_xyz_file.write(f"1 135.0 0.01 \n")
            for particle in frame_particles:
                x, v = particle
                output_xyz_file.write(f"2 {x} {R} \n")

def main():
    static_filename = "../java/main/static_2.txt"
    output_filename = './output_2_0.01.txt'
    output_xyz_filename = './output.xyz'

    # Read static data
    R, L_fixed, N = read_static_data(static_filename)



    # Read output data for each frame
    output_data = read_output_data(output_filename)

    # Create the XYZ file
    create_xyz_file(output_xyz_filename,output_data,R)

if __name__ == "__main__":
    main()