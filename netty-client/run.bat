@echo off
for /l %%i in (1,1,3500) do (
curl http://localhost:8091/send
)
pause;