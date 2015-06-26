@echo off
set /p atr="Enter action path: "

java -jar StrutsParser.jar E:\Project\DevStream\Sysper2\Presentation\WEB-INF %atr%
pause