# MyDevCAR — Orientación competencial para profesionales de TI

Aplicación web que ayuda a estudiantes y profesionales junior de TI a saber qué tan preparados están para el rol que buscan. El usuario autoevalúa sus competencias técnicas y blandas, elige un rol objetivo (por ejemplo, desarrollador backend) y la app le muestra su afinidad con ese rol, sus fortalezas, sus brechas y recursos concretos para cerrarlas.

El contenido se basa en marcos de competencias internacionales: **SFIA 9**, el modelo **UComGSP** y la **Stack Overflow Developer Survey 2025**, seleccionados mediante una revisión sistemática de literatura (PRISMA).

> **Demo:** [completar con el enlace de Render]
>
> Este repositorio es un fork de [m2000pro/MyDevCar](https://github.com/m2000pro/MyDevCar). Sobre el proyecto original agrega: credenciales de la base de datos en variables de entorno, un Dockerfile que compila la app desde el código fuente, un `.gitignore` para los archivos compilados y esta documentación.

## Qué hace

| Módulo | Función |
| --- | --- |
| Registro e inicio de sesión | Cuentas de usuario con contraseñas cifradas (BCrypt) |
| Registro de competencias | El usuario indica su nivel en competencias blandas y técnicas, agrupadas por categoría |
| Roles y habilidades | Elige un rol objetivo y registra las habilidades específicas de ese rol |
| Resultados | Resumen de competencias, afinidad por rol, fortalezas, brechas y oportunidades, con gráficos |
| Recomendaciones | Cursos, documentación y miniproyectos para cerrar cada brecha, generales o según el rol |

## Validación

Se probó con 25 estudiantes de Ingeniería de Sistemas de la UNFV (4.º a 10.º ciclo) mediante un cuestionario de escala Likert:

- El **88 %** consideró que el diagnóstico de MyDevCAR es superior a una autoevaluación manual.
- El **92 %** calificó la interfaz como intuitiva y fácil de usar.
- El **84 %** afirmó que conocer su afinidad por rol cambió su percepción de sus objetivos de carrera.

## Arquitectura

El proyecto sigue el patrón **Modelo-Vista-Controlador (MVC)**:

```
PrototipoLimpio/
├── src/java/com/cvproy/
│   ├── controller/   Servlets: reciben las peticiones y llaman al modelo
│   ├── model/        Reglas de negocio y acceso a datos (Usuario, Rol, Habilidad, Competencia...)
│   └── util/         Conexión a la base de datos
├── web/
│   ├── index.jsp     Inicio de sesión
│   └── View/         Vistas JSP y estilos
└── *.jar             Librerías (MySQL Connector/J, BCrypt, Gson, JSTL)
```

## Tecnologías

- **Backend:** Java 21, Jakarta Servlets y JSP, Apache Tomcat 10.1
- **Base de datos:** MySQL en TiDB Cloud
- **Librerías:** jBCrypt, Gson, JSTL, MySQL Connector/J
- **Despliegue:** Docker y Render

## Cómo ejecutarlo

### Variables de entorno

La conexión a la base de datos se configura con variables de entorno, para no guardar credenciales en el código:

| Variable | Descripción | Valor por defecto |
| --- | --- | --- |
| `DB_HOST` | Servidor de la base de datos | `gateway01.us-east-1.prod.aws.tidbcloud.com` |
| `DB_PORT` | Puerto | `4000` |
| `DB_NAME` | Nombre de la base de datos | `BDCOMP` |
| `DB_USER` | Usuario | — (obligatorio) |
| `DB_PASSWORD` | Contraseña | — (obligatorio) |

### Con Docker

```bash
docker build -t mydevcar .
docker run -p 8080:8080 -e DB_USER=tu_usuario -e DB_PASSWORD=tu_contraseña mydevcar
```

Luego abre `http://localhost:8080`. El Dockerfile compila el código fuente y arma el WAR automáticamente, así que no hace falta subir archivos compilados al repositorio.

### Con NetBeans

1. Abre la carpeta `PrototipoLimpio` como proyecto en NetBeans con Apache Tomcat 10.1 y JDK 21.
2. Define `DB_USER` y `DB_PASSWORD` como variables de entorno del sistema y reinicia NetBeans.
3. Ejecuta el proyecto.

### En Render

En el servicio, ve a **Environment** y agrega `DB_USER` y `DB_PASSWORD`. Render construye la imagen con el Dockerfile en cada push.

## Equipo

| Integrante | Aportes |
| --- | --- |
| Selva Sánchez, Miguel de Jesús ([@m2000pro](https://github.com/m2000pro)) | Diseño de la base de datos (15 tablas); login y registro con contraseñas cifradas (BCrypt); conexión a la base de datos; reestructuración del código en capas con DAO; resumen de resultados con Chart.js; recomendaciones generales; ventanas de ayuda y configuración. Artículo: requisitos funcionales y no funcionales, patrón de diseño y metodología de desarrollo |
| Toro Suyllon, Gabriel David ([@gabrielsuyllon-nt](https://github.com/gabrielsuyllon-nt)) | Prototipos de pantallas; menú principal; registro de competencias técnicas por categoría; módulo de registro de roles y habilidades (selección dinámica con JSTL); detalle del resultado por rol, que clasifica las habilidades en fortalezas, brechas y oportunidades con gráficos. Artículo: arquitectura de software, descripción de los módulos y validación funcional con arquetipos de usuario |
| En conjunto | Revisión sistemática de literatura, recomendaciones por rol, despliegue en la nube y validación con 25 estudiantes |

Asesor y coautor del artículo: Petrlik Azabache, Iván Carlo.

Proyecto desarrollado en la Universidad Nacional Federico Villarreal (2025). Artículo asociado: *Desarrollo de aplicativo web para la orientación competencial de profesionales de TI en Lima Metropolitana* (no publicado).
