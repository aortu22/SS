import matplotlib.pyplot as plt
import numpy as np


# Define the file names and corresponding areas
file_names = ["impulse0.03.txt", "impulse0.05.txt", "impulse0.07.txt", "impulse0.09.txt"]
l = [0.03, 0.05, 0.07, 0.09]
areas = []

areasNegative = [1/(0.09*0.09+0.09*0.03), 1/(0.09*0.09+0.09*0.05), 1/(0.09*0.09+0.09*0.07), 1/(0.09*0.09+0.09*0.09)]

# Initialize lists to store results
averages = []
errors = []
for i in range(len(l)):
    areas.append([0.09 * 3 + (0.09 - l[i]),0.09 * 2 + l[i]])
# Loop through each file
for file_name, area in zip(file_names, areas):
    data = []
    with open(file_name, 'r') as file:
        lines = file.readlines()

    # Process each run in the file
    runs = []
    run=0
    for line in lines:
        if line.strip():  # Ignore empty lines
            run_data = line.split()
            if run_data[0] == "L" and run > 0:
                runs.append(run)
                run = 0

            if len(run_data) == 3 and run_data[0] != "L" and float(run_data[0]) > 95:

                run=run+(float(run_data[1])/area[0] + float(run_data[2])/area[1])
    runs.append(run)
    # Calculate the sum and average for each run

    for i in range(len(runs)):
        runs[i] = runs[i]/5
    run_averages = np.mean(runs)
    run_error = np.std(runs) / np.sqrt(len(runs))


    averages.append(run_averages)
    errors.append(run_error)

# Create a graph

slope = np.mean(np.divide(averages, areasNegative))

def linear_fit(x):
    return slope * x

# Crear un nuevo conjunto de datos para la línea ajustada
x_fit = np.linspace(0, max(areasNegative), 100)
y_fit = linear_fit(x_fit)

# Crear el gráfico de dispersión de los datos originales
plt.plot(areasNegative, averages, label='Datos originales')

# Agregar la línea ajustada al mismo gráfico
plt.plot(x_fit, y_fit, label=f'Línea ajustada: y = {slope:.2f}x', color='red')

colors = ['red', 'green', 'blue', 'purple']
color_line = 'orange'
# Create a figure and axis
fig, ax = plt.subplots()

# Plot each data point with a different color
for i in range(len(areasNegative)):
    ax.errorbar(areasNegative[i], averages[i], yerr=errors[i], fmt='o', linestyle='-',color=colors[i], label=f'L = {l[i]}', capsize=5)
    if i > 0:
        ax.plot([areasNegative[i-1], areasNegative[i]], [averages[i-1], averages[i]], color=color_line, linestyle='-', linewidth=2)

# plt.errorbar(areasNegative, averages, yerr=errors, marker='o', linestyle='-', capsize=5)

# for i, x_val in enumerate(areasNegative):
#     y_val = averages[i]
#     plt.text(x_val, y_val, f'L={l[i]}', ha='right', va='bottom')

plt.xlabel('A^-1 (1/m^2)')
plt.ylabel('Presión (Kg/s^2)')
ax.legend()
plt.grid(True)
plt.show()