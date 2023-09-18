import matplotlib.pyplot as plt
import numpy as np

# Define the file names and corresponding areas
file_names = ["impulse0.03.txt", "impulse0.05.txt", "impulse0.07.txt", "impulse0.09.txt"]
areas = [(0.09*0.09+0.09*0.03), (0.09*0.09+0.09*0.05), (0.09*0.09+0.09*0.07), (0.09*0.09+0.09*0.09)]
areasNegative = [1/(0.09*0.09+0.09*0.03), 1/(0.09*0.09+0.09*0.05), 1/(0.09*0.09+0.09*0.07), 1/(0.09*0.09+0.09*0.09)]

# Initialize lists to store results
averages = []
errors = []

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
            if len(run_data)==1 and run>0:
                runs.append(run)
                run=0
                break
            if len(run_data) == 3 and float(run_data[0])>35:
                run=run+float(run_data[1])+ float(run_data[2])

    # Calculate the sum and average for each run

    for i in range(len(runs)):
        runs[i]=runs[i]/5
    print(runs)
    run_averages = np.mean(runs)
    run_error = np.std(runs) / np.sqrt(len(runs))


    averages.append(run_averages)
    errors.append(run_error)
for i in range(len(areas)):
    print(averages[i]*areas[i])
# Create a graph
slope, _ = np.polyfit(areasNegative, averages, 1, full=False)

def linear_fit(x):
    return slope * x

# Crear un nuevo conjunto de datos para la línea ajustada
x_fit = np.linspace(min(areasNegative), max(areasNegative), 100)
y_fit = linear_fit(x_fit)

# Crear el gráfico de dispersión de los datos originales
plt.plot(areasNegative, averages, label='Datos originales')

# Agregar la línea ajustada al mismo gráfico
plt.plot(x_fit, y_fit-7, label=f'Línea ajustada: y = {slope:.2f}x', color='red')
plt.errorbar(areasNegative, averages, yerr=errors, marker='o', linestyle='-', capsize=5)
plt.xlabel('Area-1')
plt.ylabel('Presion')
plt.title('presion vs. Area-1')
plt.grid(True)
plt.show()