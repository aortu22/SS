def main():

    # Abre el archivo en modo lectura
    with open('./output_with_angles.txt', "r") as archivo:
        # Lee todas las líneas del archivo
        lineas = archivo.readlines()

    # Elimina la primera línea, que contiene los encabezados
    encabezados = lineas[0]
    lineas = lineas[1:]

    # Ordena las líneas por el valor de "frame"
    lineas_ordenadas = sorted(lineas, key=lambda linea: int(linea.split('\t')[0]))

    # Vuelve a agregar los encabezados
    resultado = encabezados + ''.join(lineas_ordenadas)

    # Abre un nuevo archivo en modo escritura y escribe el resultado
    with open('./output_with_angles_order.txt', "w") as archivo_ordenado:
        archivo_ordenado.write(resultado)
if __name__ == '__main__':
    main()