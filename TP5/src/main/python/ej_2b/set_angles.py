import math

def main():
    with open('./output_with_angles.txt', "w") as archivo_destino:
        with open('./output_with_velocity.txt', 'r') as archivo:
            lineas = archivo.readlines()
            # Iterar sobre las líneas y extraer las velocidades
            archivo_destino.write(lineas[0].rstrip("\n") + "\tangle\n")
            for i in range(1, 26):
                angle = ''
                prev_x = ''
                prev_y = ''
                j = i
                for _ in range(251):
                    palabras = lineas[j].split()
                    if prev_x == '' and prev_y == '':
                        angle = '0'
                    else:
                        # Calcular la diferencia en coordenadas
                        delta_x = float(prev_x) - float(palabras[1])
                        delta_y = float(prev_y) - float(palabras[2])
                        # Calcular el ángulo en radianes
                        angulo_radianes = math.atan2(delta_y, delta_x)

                        # Convertir el ángulo a grados
                        angle = math.degrees(angulo_radianes)
                    archivo_destino.write(lineas[j].rstrip("\n") + "\t" + str(angle) + "\n")
                    prev_x = palabras[1]
                    prev_y = palabras[2]
                    j += 25
        archivo.close()
    archivo_destino.close()
if __name__ == '__main__':
    main()
