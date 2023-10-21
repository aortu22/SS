import matplotlib.pyplot as plt
def main():
    # Posiciones de las pelotas
    posiciones = [13.5,36.0,81.0,67.5,112.5,54.0,40.5,103.5,49.5,31.5,63.0,9.0,72.0,90.0,99.0,0.0,58.5,76.5,85.5,18.0,117.0,4.5,27.0,94.5, 126.0, 45.0, 22.5, 121.5, 108.0, 130.5]
    # Radio de las pelotas
    radio = 2.25

    # Longitud total de la línea
    longitud_linea = 135.0

    # Crea una figura y ejes
    fig, ax = plt.subplots()

    # Dibuja la línea
    ax.plot([0, longitud_linea], [0, 0], color='black', linewidth=2)

    # Dibuja las pelotas en las posiciones especificadas
    for posicion in posiciones:
        ax.add_patch(plt.Circle((posicion, 0), radio, color='red'))

    # Ajusta los límites de los ejes
    ax.set_xlim(0, longitud_linea)
    ax.set_ylim(-radio, radio)

    # Configura el aspecto de la gráfica
    plt.gca().set_aspect('equal', adjustable='box')

    # Título y etiquetas de los ejes
    plt.title('Línea con Pelotas')
    plt.xlabel('Posición (cm)')
    plt.ylabel('Altura (cm)')

    # Muestra la gráfica
    plt.show()


if __name__ == "__main__":
    main()