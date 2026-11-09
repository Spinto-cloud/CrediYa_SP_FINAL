<img width="1536" height="1024" alt="image" src="https://github.com/user-attachments/assets/85ea9c6c-c1cb-40ee-a0e9-9627bad0d21e" />

# 💳 CrediYa SP
### Sistema de Gestión de Cartera

<p align="center">
  <img src="https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white" />
  <img src="https://img.shields.io/badge/Maven-C71A36?style=for-the-badge&logo=apachemaven&logoColor=white" />
  <img src="https://img.shields.io/badge/MySQL-4479A1?style=for-the-badge&logo=mysql&logoColor=white" />
  <img src="https://img.shields.io/badge/JDBC-Database-blue?style=for-the-badge" />
  <img src="https://img.shields.io/badge/NetBeans-1B6AC6?style=for-the-badge&logo=apachenetbeanside&logoColor=white" />
</p>

<p align="center">
  <strong>Una solución para administrar clientes, empleados, préstamos, cuotas y pagos de manera organizada y eficiente.</strong>
</p>

---

## 📌 Sobre el proyecto

**CrediYa SP** es una aplicación de escritorio basada en consola, desarrollada en Java, orientada a la gestión y administración de cartera de una empresa de créditos personales.

El sistema permite centralizar la información de clientes y empleados, registrar préstamos, controlar cuotas, gestionar pagos y generar reportes para facilitar el seguimiento de las obligaciones financieras.

Este proyecto académico aplica conceptos de programación orientada a objetos, arquitectura MVC, principios SOLID, patrones de diseño y persistencia de datos mediante MySQL y archivos TXT.

## 🎯 Objetivo

Desarrollar un sistema modular que permita gestionar la información de los clientes, controlar los préstamos otorgados, registrar pagos y consultar reportes para facilitar el seguimiento de la cartera.

## 🧰 Tecnologías utilizadas

| Tecnología | Aplicación |
|---|---|
| ☕ Java | Desarrollo de la lógica del sistema |
| 💻 Apache NetBeans | Entorno de desarrollo |
| 📦 Maven | Gestión y construcción del proyecto |
| 🐬 MySQL | Administración de la base de datos |
| 🔌 JDBC | Conexión entre Java y MySQL |
| 🧩 Collections | Manejo de colecciones de objetos |
| ⚡ Lambda y Stream API | Procesamiento y generación de reportes |
| 📁 Archivos TXT | Persistencia y sincronización de información |

## 🚀 Funcionalidades principales

<table>
<tr>
<td width="50%" valign="top">

### 👥 Gestión de empleados

- Registrar empleados.
- Consultar empleados.
- Administrar información laboral.

</td>
<td width="50%" valign="top">

### 🧑‍💼 Gestión de clientes

- Registrar clientes.
- Consultar clientes.
- Consultar información de clientes.

</td>
</tr>
<tr>
<td width="50%" valign="top">

### 💰 Gestión de préstamos

- Registrar préstamos.
- Asociar clientes y empleados.
- Consultar préstamos.
- Consultar cuotas.
- Identificar préstamos en mora.
- Consultar cuotas vencidas.

</td>
<td width="50%" valign="top">

### 💳 Gestión de pagos

- Registrar pagos.
- Consultar historial de pagos.
- Actualizar valores pendientes.
- Sincronizar préstamos y cuotas.

</td>
</tr>
</table>

### 📊 Módulo de reportes

El sistema permite generar diferentes consultas para conocer el estado de la cartera:

- Préstamos activos.
- Préstamos cancelados.
- Clientes con deuda.
- Préstamos en mora.
- Resumen general de cartera.

## 🏗️ Arquitectura del proyecto

El sistema utiliza una estructura basada en **MVC**, separando responsabilidades para facilitar la organización, mantenimiento y evolución del código.

```text
CrediYa_SP/
│
├── src/
│   └── main/
│       └── java/
│           ├── model/
│           ├── view/
│           ├── controller/
│           ├── repository/
│           ├── database/
│           ├── factory/
│           └── main/
│
├── data/
├── docs/
│   ├── uml/
│   └── screenshots/
│
├── pom.xml
└── README.md
```

## 🗄️ Persistencia de datos

El proyecto implementa mecanismos de persistencia para mantener la información del sistema:

- **MySQL:** almacenamiento de empleados, clientes, préstamos, cuotas y pagos.
- **JDBC:** comunicación entre la aplicación Java y la base de datos.
- **Archivos TXT:** almacenamiento y sincronización de información.

**Base de datos:** `crediya_sp_db`

## 🧠 Patrones y principios de diseño

| Patrón o principio | Aplicación |
|---|---|
| MVC | Separación entre modelo, vista y controlador |
| DAO | Interfaces para definir operaciones de acceso a datos |
| Repository | Implementación de operaciones de persistencia |
| Factory | Creación centralizada de controladores mediante `ControllerFactory` |
| SOLID | Organización y separación de responsabilidades |

## 🖥️ Evidencias del funcionamiento

A continuación se presentan capturas reales de los módulos y funcionalidades del sistema.

<details>
<summary><strong>🏠 1. Menú principal</strong></summary>

<img width="397" alt="Menú principal" src="https://github.com/user-attachments/assets/60ffc83c-c1cf-47c1-9d0c-15b7c4ed09ed" />

</details>

<details>
<summary><strong>👥 2. Gestión de clientes</strong></summary>

**Consulta de clientes**

<img width="336" alt="Consulta de clientes" src="https://github.com/user-attachments/assets/c0d2f666-ea5f-46dd-a434-ef97217a0e6b" />

**Registro de clientes**

<img width="397" alt="Registro de clientes" src="https://github.com/user-attachments/assets/5adea7dc-7089-4f17-a1d9-9d77132061f0" />

**Confirmación de registro**

<img width="380" alt="Cliente guardado" src="https://github.com/user-attachments/assets/904ccdad-43c2-4e49-bde9-d8566502a386" />

</details>

<details>
<summary><strong>👨‍💼 3. Gestión de empleados</strong></summary>

**Consulta de empleados**

<img width="342" alt="Consulta de empleados" src="https://github.com/user-attachments/assets/7f8ca3d5-d451-4505-bc01-d98e5597b47e" />

**Registro de empleados**

<img width="397" alt="Registro de empleados" src="https://github.com/user-attachments/assets/18841a2a-e5aa-485c-b5fb-2e4d6f1ed9ba" />

**Confirmación de registro**

<img width="397" alt="Empleado guardado" src="https://github.com/user-attachments/assets/ac7aa704-7849-48c1-9c6c-0473a733d615" />

</details>

<details>
<summary><strong>💰 4. Gestión de préstamos</strong></summary>

**Consulta de préstamos**

<img width="479" alt="Consulta de préstamos" src="https://github.com/user-attachments/assets/5cbf2504-bba5-40d5-8a08-780732618989" />

**Registro de préstamos**

<img width="399" alt="Registro de préstamos" src="https://github.com/user-attachments/assets/dc51708d-514d-4152-912b-036a8ad4e8da" />

**Confirmación de registro**

<img width="660" alt="Préstamo guardado" src="https://github.com/user-attachments/assets/e46d565a-e654-42ad-8200-6c3223d57f93" />

</details>

<details>
<summary><strong>📅 5. Gestión de cuotas</strong></summary>

**Consulta de cuotas**

<img width="414" alt="Consulta de cuotas" src="https://github.com/user-attachments/assets/98ed40d2-7c4a-4cb0-8414-f9e2856b3ac3" />

**Consulta de cuotas vencidas**

<img width="327" alt="Cuotas vencidas" src="https://github.com/user-attachments/assets/8202083b-4d81-4a90-bff5-b59f24ba6bb9" />

**Consulta de préstamos en mora**

<img width="313" alt="Préstamos en mora" src="https://github.com/user-attachments/assets/f23c5a03-6ae8-434a-9626-89f3e8f0eaa7" />

</details>

<details>
<summary><strong>💳 6. Gestión de pagos</strong></summary>

**Registro de pagos**

<img width="395" alt="Registro de pago" src="https://github.com/user-attachments/assets/981ba55f-6bb0-4d27-89ac-14a3c367ebe1" />

**Confirmación de pago y transacción**

<img width="658" alt="Pago registrado" src="https://github.com/user-attachments/assets/c92914be-cdbd-4c2a-9900-f79ea1fccc07" />

**Historial de pagos**

<img width="325" alt="Consulta de pagos" src="https://github.com/user-attachments/assets/4a0efe7a-5093-4e81-abde-b4d67cf42650" />

</details>

<details>
<summary><strong>📊 7. Reportes y resumen de cartera</strong></summary>

**Préstamos activos**

<img width="414" alt="Reporte de préstamos activos" src="https://github.com/user-attachments/assets/3ac7dbea-3c66-42f2-bc14-a759778eeead" />

**Clientes con deuda**

<img width="301" alt="Reporte de clientes con deuda" src="https://github.com/user-attachments/assets/3077b7d5-2f31-4a98-91f4-b606bd63d2f5" />

**Préstamos en mora**

<img width="313" alt="Reporte de préstamos en mora" src="https://github.com/user-attachments/assets/73eca7c1-c047-43fe-93ce-56e7c73000eb" />

**Préstamos cancelados**

<img width="367" alt="Reporte de préstamos cancelados" src="https://github.com/user-attachments/assets/93988e8b-c731-490c-acc5-36d73db0ba06" />

**Resumen general**

<img width="478" alt="Resumen general" src="https://github.com/user-attachments/assets/d1efbcac-7c5a-4c49-a36f-9034e82caf5e" />

</details>

<details>
<summary><strong>✅ 8. Finalización del programa</strong></summary>

<img width="814" alt="Salida del programa y BUILD SUCCESS" src="https://github.com/user-attachments/assets/2157f552-f92e-42bc-bced-8ba24b8e54c3" />

</details>

## 📐 Diagramas UML

Los diagramas UML permiten visualizar la estructura, componentes y comportamiento del sistema.

<details>
<summary><strong>📦 Diagrama de clases</strong></summary>

<img width="1098" alt="Diagrama UML de clases" src="https://github.com/user-attachments/assets/bcc52053-e8f9-4af5-9b8b-3c1d41b0e767" />

</details>

<details>
<summary><strong>🧩 Diagrama de componentes</strong></summary>

<img width="3582" alt="Diagrama UML de componentes" src="https://github.com/user-attachments/assets/78669c4b-f27f-4ccd-8f36-2a49947e9530" />

</details>

<details>
<summary><strong>👤 Diagrama de casos de uso</strong></summary>

<img width="1618" alt="Diagrama UML de casos de uso" src="https://github.com/user-attachments/assets/2fa939c4-ee69-459a-8a6c-6d7c3f6b5c31" />

</details>

<details>
<summary><strong>🔄 Diagrama de secuencia</strong></summary>

<img width="4012" alt="Diagrama UML de secuencia" src="https://github.com/user-attachments/assets/e64b1f86-80be-4d1a-91bb-b9690b1114d8" />

</details>

<details>
<summary><strong>⚙️ Diagrama de actividades</strong></summary>

<img width="1700" alt="Diagrama UML de actividades" src="https://github.com/user-attachments/assets/ce272d33-d5df-4881-bd83-5e6efd8d5541" />

</details>

<details>
<summary><strong>🖥️ Diagrama de despliegue</strong></summary>

<img width="862" alt="Diagrama UML de despliegue" src="https://github.com/user-attachments/assets/3da614f7-a900-4d23-aa32-df7471f5dd78" />

</details>

## ⚙️ Instalación y ejecución

1. Clonar el repositorio:

```bash
git clone URL_DEL_REPOSITORIO
```

2. Abrir el proyecto en Apache NetBeans.
3. Configurar las credenciales de conexión a MySQL.
4. Crear la base de datos `crediya_sp_db` y sus respectivas tablas.
5. Verificar las dependencias de Maven.
6. Ejecutar la clase principal del proyecto.

> **Importante:** configura las credenciales de la base de datos de forma segura. No publiques contraseñas ni información privada en GitHub.

## 👨‍💻 Autor

<p align="center">
  <strong>Sergio Pinto</strong><br/>
  K4 · Campuslands
</p>

<p align="center">
  Proyecto académico de desarrollo de software.<br/>
  Aplicando Java, programación orientada a objetos, arquitectura MVC y gestión de bases de datos.
</p>

---

<p align="center">
  ⭐ <strong>CrediYa SP</strong>
