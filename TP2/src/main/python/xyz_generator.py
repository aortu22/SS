def read_static_data(static_filename):
# Read and extract wall length, number of particles, and frame distance
# Return wall_length, num_particles, frame_distance
    with open(static_filename, 'r') as static_file:
    # Read the first line to get wall length
        wall_length = float(static_file.readline().strip())

        # Read the second line to get number of particles
        num_particles = int(static_file.readline().strip())

        # Read the third line to get frame distance
        frame_distance = float(static_file.readline().strip())
    return wall_length, num_particles, frame_distance


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
            x, y, angle = map(float, line.split())
            particles.append((x, y, angle))

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
                x, y, angle = map(float, particle_data)
                frame_particles.append((x, y, angle))
                i += 1

            output_data.append((time, frame_particles))

            i += 1  # Skip the empty line between frames

    return output_data


def create_xyz_file(output_xyz_filename, static_walls, dynamic_particles_data, output_data):
    with open(output_xyz_filename, 'w') as output_xyz_file:
        # Write the first frame (static walls + dynamic particles from first frame)
        total_particles = len(static_walls) + len(dynamic_particles_data)
        output_xyz_file.write(f"{total_particles}\n")

        # Write static wall particles
        for wall in static_walls:
            output_xyz_file.write(f"W {wall[0]} {wall[1]}\n")

        # Write dynamic particles for the first frame
        for particle in dynamic_particles_data:
            x, y, angle = particle
            output_xyz_file.write(f"D {x} {y} {angle}\n")

        # Write subsequent frames from output_data
        for time, frame_particles in output_data:
            total_particles = len(frame_particles)
            output_xyz_file.write(f"{total_particles}\n")

            # Write dynamic particles for the frame
            for particle in frame_particles:
                x, y, angle = particle
                output_xyz_file.write(f"D {x} {y} {angle}\n")


def create_static_walls(wall_length):
    # Create the four corners of the square wall
    corner1 = (0.0, 0.0)
    corner2 = (0.0, wall_length)
    corner3 = (wall_length, wall_length)
    corner4 = (wall_length, 0.0)

    static_walls = [corner1, corner2, corner3, corner4]
    return static_walls

def main():
    static_filename = "../java/main/static.txt"
    dynamic_filename = "../java/main/dynamic.txt"
    output_filename = './output.txt'
    output_xyz_filename = './output.xyz'

    # Read static data
    wall_length, num_particles, frame_distance = read_static_data(static_filename)

    # Create the static walls
    static_walls = create_static_walls(wall_length)  # List of wall particle data (x, y, z) for each corner

    # Read dynamic data for the first frame
    time, particles = read_dynamic_data(dynamic_filename, num_particles)

    # Read output data for each frame
    output_data = read_output_data(output_filename)

    # Create the XYZ file
    create_xyz_file(output_xyz_filename, static_walls, particles,output_data)

if __name__ == "__main__":
    main()