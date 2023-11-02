import numpy as np
import matplotlib.pyplot as plt
from matplotlib.animation import FuncAnimation
from ase import Atoms

def update(num, frames, sc, colors, sc_trail, crosses):
    """Update function for animation."""
    frame = frames[num]
    sc.set_offsets(frame[:, :2])  # Only take x and y coordinates for 2D

    num_particles = len(frame)

    # Update the trail for each particle
    for i in range(num_particles):
        trail_data[i].append(frame[i, :2])
        if len(trail_data[i]) > trail_length:
            trail_data[i].pop(0)  # Remove the oldest point from the trail

    # Set unique colors for each particle
    sc.set_color(colors)

    # Update the trail scatter plot
    trail_points = np.vstack([trail_data[i] for i in range(num_particles)])
    sc_trail.set_offsets(trail_points)
    sc_trail.set_color([colors[i] for i in range(num_particles) for _ in range(len(trail_data[i]))])

    return sc, sc_trail, crosses

def parse_xyz(filename):
    """Parse XYZ file and return a list of Atoms objects for each frame."""
    frames = []

    with open(filename, 'r') as file:
        while True:
            try:
                # Read number of atoms
                num_atoms = int(file.readline())
            except ValueError:
                # End of file
                break

            # Read comment line
            file.readline()

            # Read atomic positions
            positions = []
            for _ in range(num_atoms):
                line = file.readline().split()
                positions.append([float(line[1]), -float(line[2]), 0])

            # Create Atoms object and append to frames
            frame = Atoms(numbers=np.ones(num_atoms), positions=positions)
            frames.append(frame)

    return frames

# Parse the XYZ file
frames = parse_xyz("output2c.xyz")
frames_np = [np.array(frame.positions) for frame in frames]

# Number of frames to keep in the trail
trail_length = 10

# Initialize the trail data structure
trail_data = [[] for _ in range(26)]

# Define coordinates for crosses
cross_coordinates = [(2.0, 2.0), (-2.0, -2.0)]  # Add your coordinates here

# Generate random colors for each particle
num_particles = 26
colors = ['red' if i == 25 else 'black' for i in range(num_particles)]  # RGB colors

# Set up the figure and axis
fig, ax = plt.subplots()
sc = ax.scatter([], [])

# Setting the limits (you might want to adjust these based on your data)
ax.set_xlim([-13.5, 13.5])
ax.set_ylim([-8, 8])

# Create a scatter plot for the trail
sc_trail = ax.scatter([], [], s=2)  # You can adjust the size 's' as needed

# Create crosses at specified coordinates
crosses = ax.scatter([-3.25,-3.25,-3.25,0,0,0,3.25,3.25,3.25,9.75,9.75,9.75,-9.75,-9.75,-9.75,12.7,12.7,12.7,-13,-13,-13], [6.5,0,-6.6,0,3.25,-3.25,6.5,0,-6.6,6.5,0,-6.6,6.5,0,-6.6,6.5,0,-6.6,6.5,0,-6.6], marker='x', color='black', s=15)  # Customize marker style and color

# Remove axis marks and numbers
ax.set_xticks([])
ax.set_yticks([])

# Create the animation
ani = FuncAnimation(fig, update, frames=len(frames_np), fargs=(frames_np, sc, colors, sc_trail, crosses), interval=125)  # 8 fps

# Save the animation to a GIF file (you can change the format if needed)
# ani.save("animation.gif", writer="pillow")

plt.show()