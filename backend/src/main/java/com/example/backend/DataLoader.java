package com.example.backend;

import com.example.backend.entity.Skill;
import com.example.backend.entity.Project;
import com.example.backend.entity.WorkExperience;
import com.example.backend.repository.SkillRepository;
import com.example.backend.repository.ProjectRepository;
import com.example.backend.repository.WorkExperienceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.HashSet;
import java.util.Set;

@Component
public class DataLoader implements CommandLineRunner {

        @Autowired
        private SkillRepository skillRepository;

        @Autowired
        private ProjectRepository projectRepository;

        @Autowired
        private WorkExperienceRepository workExperienceRepository;

        @Override
        public void run(String... args) throws Exception {
                if (skillRepository.count() == 0) {
                        loadSkills();
                }

                if (projectRepository.count() < 20) {
                        projectRepository.deleteAll();
                        loadProjects();
                }

                if (workExperienceRepository.count() == 0) {
                        loadWorkExperience();
                }
        }

        private void loadSkills() {
                List<Skill> skills = Arrays.asList(
                                createSkill("STM32", "ARM microcontroller programming STM32 series", 95,
                                                Skill.SkillCategory.MICROCONTROLLERS, 1),
                                createSkill("ESP32", "IoT devices development on ESP32 with ESP-IDF", 90,
                                                Skill.SkillCategory.MICROCONTROLLERS, 2),
                                createSkill("Texas Instruments Tiva", "Tiva microcontrollers programming", 85,
                                                Skill.SkillCategory.MICROCONTROLLERS, 3),
                                createSkill("Atmel AVR", "Atmel microcontrollers programming", 80,
                                                Skill.SkillCategory.MICROCONTROLLERS, 4),

                                createSkill("Omron PLC", "Industrial Omron controllers programming", 90,
                                                Skill.SkillCategory.PLC, 5),
                                createSkill("JUMO mTRON T", "JUMO controllers programming", 85,
                                                Skill.SkillCategory.PLC, 6),
                                createSkill("CodeSys", "PLC programming in CodeSys environment", 85,
                                                Skill.SkillCategory.PLC, 7),
                                createSkill("SCADA systems", "SCADA systems development", 90,
                                                Skill.SkillCategory.PLC, 8),

                                createSkill("Modbus", "Modbus RTU/TCP protocol implementation", 85,
                                                Skill.SkillCategory.PROTOCOLS, 9),
                                createSkill("WiFi", "WiFi connections and IoT devices implementation", 85,
                                                Skill.SkillCategory.PROTOCOLS, 10),
                                createSkill("GSM/SMS", "GSM modules with SMS support development", 80,
                                                Skill.SkillCategory.PROTOCOLS, 11),

                                createSkill("C/C++", "Microcontroller programming in C/C++", 95,
                                                Skill.SkillCategory.PROGRAMMING, 12),
                                createSkill("Java Spring Boot", "Backend development with Java Spring Boot", 85,
                                                Skill.SkillCategory.PROGRAMMING, 13),
                                createSkill("PostgreSQL", "PostgreSQL database design", 80,
                                                Skill.SkillCategory.PROGRAMMING, 14),

                                createSkill("Altium Designer", "PCB design", 90,
                                                Skill.SkillCategory.TOOLS, 15),
                                createSkill("Circuit Design", "Electronic circuit development", 95,
                                                Skill.SkillCategory.HARDWARE, 16));

                skillRepository.saveAll(skills);
        }

        private void loadProjects() {
                // Создаём проекты с реальными данными из портфолио Тимура
                List<Project> projects = Arrays.asList(
                                // === STM32 ПРОЕКТЫ ===
                                createProject("USB Bluetooth устройство для швейных машин Janome",
                                                "USB Bluetooth Mass storage устройство для швейных машинок Janome. Передача файлов картинок с телефона по Bluetooth протоколу для загрузки швейной машинкой.",
                                                Project.ProjectCategory.STM32, Project.ProjectStatus.COMPLETED, true, 1,
                                                Arrays.asList("ARM микроконтроллер", "Bluetooth", "USB Mass Storage",
                                                                "Android", "C/C++"),
                                                "/images/projects/webp/janome.webp",
                                                new HashSet<>(Arrays.asList(
                                                                "/images/projects/webp/janome.webp",
                                                                "/images/projects/webp/janome1.webp",
                                                                "/images/projects/webp/janome2.webp",
                                                                "/images/projects/webp/janome3.webp")),
                                                "Инновационное решение для современного рукоделия. Устройство обеспечивает беспроводную передачу дизайнов вышивки с мобильного телефона на швейные машины Janome."),

                                createProject("Сигнализатор утечки газа автомобильный",
                                                "Контроль утечки газа в автомобиле по трем зонам: кабина, моторный отсек, багажник. Построен на 32-разрядном ARM микроконтроллере с надежными датчиками газа.",
                                                Project.ProjectCategory.STM32, Project.ProjectStatus.COMPLETED, true, 2,
                                                Arrays.asList("STM32", "ARM 32-bit", "Датчики газа",
                                                                "Android Bluetooth", "C/C++",
                                                                "Температурная компенсация"),
                                                "/images/projects/webp/gas_auto.webp",
                                                new HashSet<>(Arrays.asList(
                                                                "/images/projects/webp/gas_auto.webp",
                                                                "/images/projects/webp/gas_auto1.webp",
                                                                "/images/projects/webp/gas_auto2.webp",
                                                                "/images/projects/webp/gas_auto3.webp",
                                                                "/images/projects/webp/gas_auto4.webp",
                                                                "/images/projects/webp/gas_auto5.webp",
                                                                "/images/projects/webp/gas_auto6.webp",
                                                                "/images/projects/webp/gas_auto7.webp",
                                                                "/images/projects/webp/gas_auto8.webp",
                                                                "/images/projects/webp/gas_auto9.webp"
                                                        )),
                                                "Система безопасности для газовых автомобилей. Звуковая и световая индикация, выходы для реле отключения клапана подачи газа. Android приложение для мониторинга."),

                                createProject("Стабилизатор переменного напряжения 20кВт",
                                                "20кВт релейный стабилизатор переменного напряжения на ARM контроллере STM32L152. Экран отображает напряжение и ток нагрузки с защитой по току.",
                                                Project.ProjectCategory.STM32, Project.ProjectStatus.COMPLETED, true, 3,
                                                Arrays.asList("STM32L152", "Релейная коммутация", "TFT дисплей",
                                                                "Защита по току", "20кВт"),
                                                        "/images/projects/webp/stab.webp",
                                                new HashSet<>(Arrays.asList(
                                                        "/images/projects/webp/stab.webp",
                                                        "/images/projects/webp/stab1.webp",
                                                        "/images/projects/webp/stab2.webp",
                                                        "/images/projects/webp/stab3.webp"

                                                )),
                                                "Мощный релейный стабилизатор для промышленных применений с интеллектуальным управлением и полной защитой."),

                                createProject("Щит управления 4 насосами",
                                                "Щит управления 4 насосами с датчиком давления 4-20мА. Выполнен на базе микроконтроллера ARM STM32L152 с индикацией режимов работы и настройками.",
                                                Project.ProjectCategory.STM32, Project.ProjectStatus.COMPLETED, false,
                                                4,
                                                Arrays.asList("STM32L152", "4-20мА датчик", "Управление насосами",
                                                                "TFT дисплей"),
                                                        "/images/projects/webp/pump.webp",
                                                new HashSet<>(Arrays.asList(
                                                        "/images/projects/webp/pump.webp"
                                                )),
                                                        "Комплексная система управления насосным оборудованием с автоматическим поддержанием давления и защитными функциями."),

                                createProject("Щит управления насосами подогрева воды",
                                                "Управление котлом подогрева воды на газе с внешним управлением. Два датчика наличия воды в трубопроводе и суточный таймер.",
                                                Project.ProjectCategory.STM32, Project.ProjectStatus.COMPLETED, false,
                                                5,
                                                Arrays.asList("STM32", "Датчики воды", "Суточный таймер", "Контакторы"),
                                                "/images/projects/webp/water.webp",
                                                new HashSet<>(Arrays.asList(
                                                        "/images/projects/webp/water.webp",
                                                        "/images/projects/webp/water1.webp",
                                                        "/images/projects/webp/water2.webp",
                                                        "/images/projects/webp/water3.webp",
                                                        "/images/projects/webp/water4.webp"

                                                )),
                                                "Автоматизированная система управления подогревом воды с полной защитой от сухого хода и программируемыми режимами работы."),

                                // === ESP32/IoT ПРОЕКТЫ ===
                                createProject("Сигнализатор утечки газа домашний с WiFi",
                                                "Контроль уровня природного газа в домашних условиях. Датчики метана и угарного газа, работа от батареи, WiFi связь с Android телефоном, GSM модуль для СМС.",
                                                Project.ProjectCategory.ESP32, Project.ProjectStatus.COMPLETED, true, 6,
                                                Arrays.asList("ESP32", "WiFi", "GSM SIM800", "Android", "Датчики газа",
                                                                "SMS оповещения"),
                                                "/images/projects/webp/home.webp",
                                        new HashSet<>(Arrays.asList(
                                                "/images/projects/webp/home.webp",
                                                "/images/projects/webp/home1.webp",
                                                "/images/projects/webp/home2.webp",
                                                "/images/projects/webp/home3.webp",
                                                "/images/projects/webp/home4.webp",
                                                "/images/projects/webp/home5.webp",
                                                "/images/projects/webp/home6.webp",
                                                "/images/projects/webp/home7.webp"
                                        )),
                                                "Умная система безопасности дома с мобильным мониторингом, логированием событий и автоматическим оповещением об опасности."),

                                createProject("LED светильник с управлением с телефона",
                                                "Драйвер LED светильника с возможностью управления через Android приложение по WiFi. Интеграция с Google Firebase сервером.",
                                                Project.ProjectCategory.ESP32, Project.ProjectStatus.COMPLETED, true, 7,
                                                Arrays.asList("ESP32", "LED драйвер", "Android", "Firebase", "WiFi",
                                                                "Контроль освещенности"),
                                                "/images/projects/webp/led.webp",
                                                new HashSet<>(Arrays.asList(
                                                        "/images/projects/webp/led.webp",
                                                        "/images/projects/webp/led1.webp",
                                                        "/images/projects/webp/led2.webp"
                                                )),
                                                "Умное освещение с удаленным управлением яркостью, контролем температуры, влажности и освещенности через мобильное приложение."),

                                createProject("Modbus LED контроллеры",
                                                "Контроллеры управления светодиодами 2-х и 3-х канальные по сети Modbus RTU/TCP с возможностью каскадного подключения.",
                                                Project.ProjectCategory.ESP32, Project.ProjectStatus.COMPLETED, false,
                                                8,
                                                Arrays.asList("ESP32", "Modbus RTU", "Modbus TCP", "LED контроль",
                                                                "2-3 канала"),
                                                "/images/projects/webp/modbus.webp",
                                                new HashSet<>(Arrays.asList(
                                                        "/images/projects/webp/modbus.webp",
                                                        "/images/projects/webp/modbus1.webp",
                                                        "/images/projects/webp/modbus2.webp",
                                                        "/images/projects/webp/modbus3.webp",
                                                        "/images/projects/webp/modbus4.webp",
                                                        "/images/projects/webp/modbus5.webp"
                                                )),
                                                "Промышленные LED контроллеры для интеграции в системы умного освещения с поддержкой стандартных промышленных протоколов."),

                                // === AUTOMATION/ПЛК ПРОЕКТЫ ===
                                createProject("Система управления инкубатором",
                                                "Промышленная система управления инкубатором: контроль температуры и влажности на DHT-22, фазо-импульсное управление подогревом, поворот яиц.",
                                                Project.ProjectCategory.AUTOMATION, Project.ProjectStatus.COMPLETED,
                                                true, 9,
                                                Arrays.asList("Texas Instruments Tiva", "DHT-22",
                                                                "Фазо-импульсное управление", "TFT 5 дюймов"),
                                                        "/images/projects/webp/incubator.webp",
                                                new HashSet<>(Arrays.asList(
                                                        "/images/projects/webp/incubator.webp",
                                                        "/images/projects/webp/incubator1.webp"
                                                )),
                                                        "Автоматизированная система для промышленного инкубатора с точным контролем параметров и полной автоматизацией процесса."),

                                createProject("Система управления насосами подачи воды",
                                                "Система управления водоснабжением на базе ARM Tiva с 5-дюймовым TFT дисплеем. Полный контроль трех насосов с датчиком давления 4-20мА.",
                                                Project.ProjectCategory.AUTOMATION, Project.ProjectStatus.COMPLETED,
                                                true, 10,
                                                Arrays.asList("ARM Tiva", "TFT 5 дюймов", "Датчик давления 4-20мА",
                                                                "Texas Instrument", "3 насоса"),
                                                "/images/projects/webp/water_pump.webp",
                                                new HashSet<>(Arrays.asList(
                                                        "/images/projects/webp/water_pump.webp",
                                                        "/images/projects/webp/water_pump1.webp",
                                                        "/images/projects/webp/water_pump2.webp",
                                                        "/images/projects/webp/water_pump3.webp",
                                                        "/images/projects/webp/water_pump4.webp"
                                                )),
                                                "Комплексная система управления водоснабжением с интеллектуальными контроллерами и автоматическим поддержанием давления."),

                                createProject("Проект станции перегонки нефти",
                                                "Автоматизация станции перегонки нефти на логических контроллерах JUMO (Fulda, Germany). Программирование ПЛК и настройка SCADA системы.",
                                                Project.ProjectCategory.PLC, Project.ProjectStatus.COMPLETED, true, 11,
                                                Arrays.asList("JUMO mTRON T", "SCADA", "ПЛК", "Нефтепереработка",
                                                                "Автоматизация"),
                                                        "/images/projects/webp/neft.webp",
                                                new HashSet<>(Arrays.asList(
                                                        "/images/projects/webp/neft.webp",
                                                        "/images/projects/webp/neft1.webp",
                                                        "/images/projects/webp/neft2.webp",
                                                        "/images/projects/webp/neft3.webp",
                                                        "/images/projects/webp/neft4.webp",
                                                        "/images/projects/webp/neft5.webp"
                                                )),
                                                        "Крупный промышленный проект автоматизации нефтеперерабатывающего оборудования с полной диспетчеризацией процесса."),

                                createProject("Программирование контроллеров ОВЕН",
                                                "Демонстрация управления климат-контролем в помещении на PLC160 и TPM202. Управление локально и через SCADA систему.",
                                                Project.ProjectCategory.PLC, Project.ProjectStatus.COMPLETED, false, 12,
                                                Arrays.asList("ОВЕН PLC160", "TPM202", "CodeSys", "SCADA",
                                                                "Климат-контроль"),
                                                "/images/projects/webp/owen.webp",
                                                new HashSet<>(Arrays.asList(
                                                        "/images/projects/webp/owen.webp",
                                                        "/images/projects/webp/owen1.webp",
                                                        "/images/projects/webp/owen2.webp",
                                                        "/images/projects/webp/owen3.webp",
                                                        "/images/projects/webp/owen4.webp",
                                                        "/images/projects/webp/owen5.webp"
                                                )),
                                                "Система автоматизации климат-контроля с удаленным мониторингом и управлением через SCADA интерфейс."),

                                createProject("Автоматизация теплиц NETAFIM 10 гектаров",
                                                "Настройка SCADA-системы израильского производства для автоматизации парников площадью 10 гектаров с полным климат-контролем.",
                                                Project.ProjectCategory.PLC, Project.ProjectStatus.COMPLETED, true, 13,
                                                Arrays.asList("SCADA NETAFIM", "Климат-контроль", "10 гектаров",
                                                                "Автоматизация теплиц"),
                                                "/images/projects/webp/netafim.webp",
                                                new HashSet<>(Arrays.asList(
                                                        "/images/projects/webp/netafim.webp",
                                                        "/images/projects/webp/netafim1.webp",
                                                        "/images/projects/webp/netafim2.webp",
                                                        "/images/projects/webp/netafim3.webp"
                                                )),
                                                        "Масштабный проект автоматизации тепличного хозяйства с интеллектуальным контролем микроклимата и оросительных систем."),

                                // === EMBEDDED ПРОЕКТЫ ===
                                createProject("Индикатор низкого напряжения на микроконтроллере",
                                                "Индикатор низкого напряжения на базе микроконтроллера Attiny15. Срабатывание при напряжении выше 80В, максимум до 1000В, питание от батарейки.",
                                                Project.ProjectCategory.EMBEDDED, Project.ProjectStatus.COMPLETED,
                                                false, 14,
                                                Arrays.asList("Attiny15", "Индикация напряжения", "Батарейка",
                                                                "Звуковая индикация"),
                                                        "/images/projects/webp/indicator.webp",
                                                new HashSet<>(Arrays.asList(
                                                        "/images/projects/webp/indicator.webp",
                                                        "/images/projects/webp/indicator1.webp",
                                                        "/images/projects/webp/indicator2.webp"
                                                )),
                                                "Портативный прибор для контроля безопасности при работе с высоковольтными установками."),

                                createProject("Система контроля доступа",
                                                "Создание платы и написание программы для полной конфигурации системы контроля доступа с проектированием в Altium Designer.",
                                                Project.ProjectCategory.EMBEDDED, Project.ProjectStatus.COMPLETED,
                                                false, 15,
                                                Arrays.asList("Контроль доступа", "Altium Designer", "Разработка плат",
                                                                "RFID"),
                                                "/images/projects/webp/control.webp",
                                                new HashSet<>(Arrays.asList(
                                                        "/images/projects/webp/control.webp",
                                                        "/images/projects/webp/control1.webp",
                                                        "/images/projects/webp/control2.webp"
                                                )),
                                                "Комплексная система безопасности с индивидуальной настройкой уровней доступа и логированием событий."),

                                createProject("GSM модуль с поддержкой СМС на любом языке",
                                                "GSM модуль для оповещения об утечке газа с отправкой СМС на номер пользователя. Поддержка Unicode для разных языков.",
                                                Project.ProjectCategory.IOT, Project.ProjectStatus.COMPLETED, false, 16,
                                                Arrays.asList("GSM SIM800", "SMS Unicode", "Многоязычность",
                                                                "Оповещения"),
                                                "/images/projects/webp/sms.webp",
                                                new HashSet<>(Arrays.asList(
                                                        "/images/projects/webp/sms.webp",
                                                        "/images/projects/webp/sms1.webp",
                                                        "/images/projects/webp/sms2.webp",
                                                        "/images/projects/webp/sms3.webp"
                                                )),
                                        "Универсальный модуль GSM оповещений с поддержкой кириллицы и других языков для систем безопасности."),

                                // === СЕРВИСНЫЕ ПРОЕКТЫ ===
                                createProject("Управление автоматическими дверьми",
                                                "Плата управления автоматическими дверьми на базе микроконтроллера Atmega168.",
                                                Project.ProjectCategory.AUTOMATION, Project.ProjectStatus.IN_PROGRESS,
                                                false, 17,
                                                Arrays.asList("Щиты управления", "Микроконтроллерные системы",
                                                                "Индивидуальные решения"),
                                                        "/images/projects/webp/door.webp",
                                                new HashSet<>(Arrays.asList(
                                                        "/images/projects/webp/door.webp",
                                                        "/images/projects/webp/door1.webp"
                                                )),
                                        "Изготовление индивидуальных плат управления автоматическими дверьми."),
                                createProject("Блок питания 5 вольт 0.5, 2 ампера",
                                        "Разработка блока питания на 5 вольт 0.5, 2 ампера",
                                        Project.ProjectCategory.AUTOMATION, Project.ProjectStatus.IN_PROGRESS,
                                        false, 17,
                                        Arrays.asList("Схема питания","Индивидуальные решения"),
                                        "/images/projects/webp/power.webp",
                                        new HashSet<>(Arrays.asList(
                                                "/images/projects/webp/power.webp",
                                                "/images/projects/webp/power1.webp"
                                        )),
                                        "Изготовление индивидуальных плат управления автоматическими дверьми."),
                                createProject("Плата управления переключением насосов циркуляции воды",
                                        "Разработка платы управления переключением насосов циркуляции воды",
                                        Project.ProjectCategory.AUTOMATION, Project.ProjectStatus.IN_PROGRESS,
                                        false, 17,
                                        Arrays.asList("Щиты управления", "Микроконтроллерные системы",
                                                "Индивидуальные решения"),
                                        "/images/projects/webp/pump_switch.webp",
                                        new HashSet<>(Arrays.asList(
                                                "/images/projects/webp/pump_switch.webp"
                                        )),
                                        "Изготовление индивидуальных плат управления переключением насосов циркуляции воды."),

                                createProject("Ремонт пищевого оборудования",
                                                "Ремонт электроники и автоматики пищевого оборудования: пароконвектоматы, электрические печи, модернизация плат управления.",
                                                Project.ProjectCategory.AUTOMATION, Project.ProjectStatus.IN_PROGRESS,
                                                false, 18,
                                                Arrays.asList("Пищевое оборудование", "Пароконвектоматы",
                                                                "Модернизация", "Ремонт электроники"),
                                                "/images/projects/webp/pisha.webp",
                                                new HashSet<>(Arrays.asList(
                                                        "/images/projects/webp/pisha.webp",
                                                        "/images/projects/webp/pisha1.webp"
                                                )),
                                                "Специализированный сервис по восстановлению и модернизации систем управления профессионального кухонного оборудования."));

                projectRepository.saveAll(projects);
        }

        private void loadWorkExperience() {
                // Добавляем опыт работы из резюме Тимура (25 лет опыта)
                List<WorkExperience> experiences = Arrays.asList(
                                createWorkExperience("Инженер-программист", "ООО «Проект 24»", "Иннополис",
                                                LocalDate.of(2022, 2, 1), null, true,
                                                "Программирование промышленных контроллеров (SysMac Studio, CodeSys, SCADA, HMI). Разработка и отладка алгоритмов управления оборудованием. Написание ПО для встраиваемых систем на ESP32, STM32, RISC-V.",
                                                "SysMac Studio, CodeSys, SCADA, HMI, ESP32, STM32, RISC-V, C/C++, Omron",
                                                "Успешная реализация более 10 проектов автоматизации промышленного оборудования"),

                                createWorkExperience("Java-программист", "Maxima OOO", "Казань",
                                                LocalDate.of(2022, 11, 1), LocalDate.of(2023, 6, 30), false,
                                                "Разработал проект REST API для документооборота: Spring Boot, PostgreSQL, JPA, Hibernate. Валидация данных, работа с JSON. Интеграция со сторонним API. Использование DTO, Spring Security. Работа по Scrum (спринты по 2 недели). Отвечал за проектирование базы данных.",
                                                "Java, Spring Boot, PostgreSQL, JPA, Hibernate, REST API, JSON, Spring Security, Scrum",
                                                "Создание полнофункционального API для системы документооборота с нуля"),

                                createWorkExperience("Инженер-программист", "ООО \"Челны свет\"", "Набережные Челны",
                                                LocalDate.of(2021, 3, 1), LocalDate.of(2021, 12, 31), false,
                                                "Разработка контроллеров управления освещением. Создание системы удалённого мониторинга освещения. Работа с C/C++, микроконтроллерами и драйверами.",
                                                "C/C++, микроконтроллеры, драйверы освещения, удалённый мониторинг",
                                                "Разработка интеллектуальных систем управления уличным освещением"),

                                createWorkExperience("Инженер программист микроконтроллеров",
                                                "ООО \"Водий нефтегазовик энергия\"", "Ташкент",
                                                LocalDate.of(2017, 8, 1), LocalDate.of(2018, 4, 30), false,
                                                "Проектировал плату контроля утечки газа (метан, пропан, угарный газ) для автомобилей и бытового применения на языке С/С++. В управляющей плате использован микроконтроллер STM32. Автомобильная версия включает три зоны контроля, а домашняя версия оснащена функцией автоматического переключения на аккумулятор при отключении питания, а также возможностью оповещения через GSM-модуль SIM800 и Wi-Fi-модуль ESP32, RISC-V.",
                                                "STM32, ESP32, RISC-V, GSM SIM800, Wi-Fi, C/C++, датчики газа",
                                                "Создание комплексной системы безопасности для контроля утечки газа"),

                                createWorkExperience("Инженер-программист АСУ ТП", "ХО \"Восполнение\"", "Туркменистан",
                                                LocalDate.of(2015, 4, 1), LocalDate.of(2017, 7, 31), false,
                                                "Обслуживание и доработка систем автоматизации и диспетчеризации (BMS) управления зданиями в области электрооборудования и климат-контроля на базе ПЛК Honeywell и SCADA-систем. В случае выхода из строя плат управления щитами разрабатывал новые платы и писал прошивки на базе микроконтроллеров STM32, Texas Instruments и Atmel на языке С/С++, RISC-V.",
                                                "ПЛК Honeywell, SCADA, BMS, STM32, Texas Instruments, Atmel, RISC-V, C/C++",
                                                "Автоматизация управления зданиями с полным климат-контролем"),

                                createWorkExperience("Инженер-программист АСУ ТП",
                                                "Филиал компании «Групарт пумпен Фертриеб ГМБХ»", "Туркменистан",
                                                LocalDate.of(2013, 7, 1), LocalDate.of(2015, 2, 28), false,
                                                "Проектировал и разрабатывал программы для ПЛК компании JUMO (Fulda) для автоматизации системы управления станцией перегонки нефти. Собирал и запускал щиты управления КНС на базе ПЛК Danfoss. Разрабатывал и программировал щиты управления насосами, а также настраивал SCADA-систему израильского производства для автоматизации парников площадью 10 гектаров.",
                                                "JUMO ПЛК, Danfoss, SCADA NETAFIM, автоматизация нефтепереработки, тепличные комплексы",
                                                "Крупные проекты: автоматизация нефтеперерабатывающей станции и тепличного комплекса 10 га"),

                                createWorkExperience("Инженер-программист АСУ ТП",
                                                "Банк Внешнеэкономической деятельности Туркменистана", "Туркменистан",
                                                LocalDate.of(2005, 8, 1), LocalDate.of(2013, 5, 31), false,
                                                "Выполнял контроль и управление системами диспетчеризации здания банка BMS. Обслуживание и доработка систем автоматизации и диспетчеризации (BMS) управления зданиями в области электрооборудования и климат-контроля на базе ПЛК Honeywell и SCADA-систем. В случае выхода из строя плат управления щитами разрабатывал новые платы и писал прошивки на базе микроконтроллеров STM32, Texas Instruments и Atmel на языке С/С++, RISC-V.",
                                                "BMS системы, ПЛК Honeywell, SCADA, STM32, Texas Instruments, Atmel, RISC-V, C/C++",
                                                "Многолетний опыт обслуживания и модернизации систем автоматизации зданий"),

                                createWorkExperience("Инженер-программист", "Министерство хлопка Туркменистана",
                                                "Туркменистан",
                                                LocalDate.of(2000, 2, 1), LocalDate.of(2005, 6, 30), false,
                                                "Проектировал систему управления станком по отделению семян хлопка от сырца на базе мк STM32F407 и TFT SSD1963 на языке С/С++. Все платы управления и контроля спроектировал сам и изготовил, тестирование происходило в течении 2 лет, станок работает.",
                                                "STM32F407, TFT SSD1963, C/C++, проектирование плат, промышленное оборудование",
                                                "Полный цикл разработки: от проектирования до внедрения промышленного оборудования"),

                                createWorkExperience("Инженер-программист",
                                                "Научно исследовательский институт охраны здоровья матери и ребенка",
                                                "Туркменистан",
                                                LocalDate.of(1997, 5, 1), LocalDate.of(2000, 1, 31), false,
                                                "Писал программы на языке Turbo Pascal для заполнения карточек пациентов и перенос данных в базу данных. Отдел программного обеспечения.",
                                                "Turbo Pascal, базы данных, медицинские информационные системы",
                                                "Создание первых автоматизированных систем учёта пациентов"));

                workExperienceRepository.saveAll(experiences);
        }

        private Skill createSkill(String name, String description, int level, Skill.SkillCategory category,
                        int displayOrder) {
                return Skill.builder()
                                .name(name)
                                .description(description)
                                .level(level)
                                .category(category)
                                .displayOrder(displayOrder)
                                .build();
        }

        private Project createProject(String title, String description, Project.ProjectCategory category,
                        Project.ProjectStatus status, boolean featured, int displayOrder, List<String> technologies,
                        String imageUrl, Set<String> images, String detailedDescription) {
                return Project.builder()
                                .title(title)
                                .description(description)
                                .category(category)
                                .status(status)
                                .featured(featured)
                                .displayOrder(displayOrder)
                                .technologies(new HashSet<>(technologies))
                                .imageUrl(imageUrl)
                                .images(images != null ? images : new HashSet<>())
                                .detailedDescription(detailedDescription)
                                .build();
        }

        // Перегрузка для существующих вызовов без массива изображений
        private Project createProject(String title, String description, Project.ProjectCategory category,
                        Project.ProjectStatus status, boolean featured, int displayOrder, List<String> technologies,
                        String imageUrl, String detailedDescription) {
                return createProject(title, description, category, status, featured, displayOrder, technologies,
                                imageUrl, null, detailedDescription);
        }

        private WorkExperience createWorkExperience(String position, String company, String location,
                        LocalDate startDate, LocalDate endDate, boolean isCurrent,
                        String description, String technologies, String achievements) {
                WorkExperience experience = new WorkExperience();
                experience.setPosition(position);
                experience.setCompany(company);
                experience.setLocation(location);
                experience.setStartDate(startDate);
                experience.setEndDate(endDate);
                experience.setIsCurrent(isCurrent);
                experience.setDescription(description);
                experience.setTechnologies(technologies);
                experience.setAchievements(achievements);
                return experience;
        }
}
