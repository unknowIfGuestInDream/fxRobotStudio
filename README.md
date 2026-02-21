# fxRobotStudio

A JavaFX-based automated operation tool for editing, recording, importing and exporting automated workflows with image recognition support.

## Features

- **Workflow Editor** - Create and edit automated operation workflows
- **Workflow Recording** - Record mouse and keyboard actions into workflows
- **Import/Export** - Save and load workflows as `.fxr` files (XML-based)
- **Loop Execution** - Support for repeating workflow execution
- **Image Recognition** - Locate UI elements using JavaCV-based image matching
- **Cross-Platform** - Supports Windows, macOS, and Ubuntu
- **Internationalization** - English, Chinese (中文), and Japanese (日本語)
- **Theme Support** - Multiple themes via AtlantaFX

## Building

```bash
mvn clean package
```

## Running

```bash
cd app
mvn javafx:run
```

## Technology Stack

| Component | Library |
|---|---|
| UI Framework | JavaFX 21.0.10 |
| UI Components | ControlsFX |
| Themes | AtlantaFX |
| Settings | PreferencesFX |
| Image Recognition | JavaCV |
| Native Access | JNA (JPMS) |
| Global Input | jNativeHook |
| XML Processing | JAXB (Jakarta XML Binding) |
| Testing | TestFX, JUnit 5 |
| Logging | SLF4J + Logback |

## License

[MIT License](LICENSE)
