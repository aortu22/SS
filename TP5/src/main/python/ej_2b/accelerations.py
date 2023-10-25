import matplotlib.pyplot as plt


def main():
    for i in range(0, 25):
        id = i
        # Crear un diccionario para almacenar las velocidades de las partículas
        particulas = {}
        frames = [i * 4 / 30 for i in range(1, 252)]

        # Leer el archivo TXT
        with open('merged_trajectories_with_velocity.txt', 'r') as archivo:
            lineas = archivo.readlines()

        # Procesar cada línea del archivo
        for linea in lineas:
            # Dividir la línea en palabras
            palabras = linea.split()

            # Extraer los datos necesarios
            # frame = int(palabras[0])
            id_particula = float(palabras[3])
            velocidad = float(palabras[4])

            # Verificar si la partícula ya está en el diccionario
            if id_particula not in particulas:
                particulas[id_particula] = []

            # Agregar la velocidad al arreglo correspondiente
            particulas[id_particula].append(velocidad)

        title = 'Tiempo [s]'
        # Crear un gráfico para cada partícula
        plt.plot(frames, particulas[id])
        plt.title(f'Partícula {id}')
        # Configurar el gráfico
        plt.xlabel(title)
        plt.ylabel('Velocidades [m/s]')
        # plt.savefig('acc_' + title + str(id) + '.png')

        # Mostrar el gráfico
        plt.show()


if __name__ == '__main__':
    main()
