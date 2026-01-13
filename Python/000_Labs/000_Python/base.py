

from pynput.mouse import Controller
import time

# Create a mouse controller
mouse = Controller()

# Move to absolute position
mouse.position = (400, 300)
print(f"Mouse moved to {mouse.position}")
time.sleep(1)

# Move relative to current position
mouse.move(100, 50)
print(f"Mouse moved to {mouse.position}")
time.sleep(1)

# Optional: move in a square pattern
for _ in range(4):
    mouse.move(50, 0)  # right
    time.sleep(0.5)
    mouse.move(0, 50)  # down
    time.sleep(0.5)
    mouse.move(-50, 0) # left
    time.sleep(0.5)
    mouse.move(0, -50) # up
    time.sleep(0.5)

print("Mouse movement complete!")
