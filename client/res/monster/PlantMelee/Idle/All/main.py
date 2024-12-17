from PIL import Image

# Load ảnh
image = Image.open("1-sheet.png")

# Chuyển sang PNG-8
image = image.convert("P", palette=Image.ADAPTIVE, colors=256)
image.save("image_png8.png", optimize=True)
