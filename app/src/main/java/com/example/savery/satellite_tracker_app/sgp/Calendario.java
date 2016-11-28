// En versiones de Java menores que 1.4.1 tienen el problema de que
// el m�todo "setTimeInMillis()" de la clase java.util.Calendar tiene
// visibilidad protected, con lo que no puede ser aplicada a una instancia
// de Calendar. En la versi�n 1.4.1 se ha cambiado su visibilidad a p�blica.
// Para que el c�digo sea compilable por todas las versiones, he decidido
// crear una clase MiCalendario que hereda de Calendar y convierte la
// visibilidad del m�todo a public.
package com.example.savery.satellite_tracker_app.sgp;

import java.util.Calendar;

abstract class Calendario extends Calendar
{
	public void	setTimeInMillis(long millis)
	{
		this.setTimeInMillis(millis);
	}
}