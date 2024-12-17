from PIL import Image

# Mở ảnh gốc
image = Image.open("front_wall.png")  # Thay bằng đường dẫn ảnh của bạn

# Kiểm tra kích thước ảnh
width, height = image.size
# assert width == 768 and height == 96, "Ảnh phải có kích thước 768x96 pixel."

# Số ảnh con
num_sub_images = 8
sub_width = width // num_sub_images  # Kích thước mỗi ảnh con (96x96)

# Danh sách lưu ảnh con đã xoay
rotated_images = []

# Chia ảnh và xoay từng ảnh con
for i in range(num_sub_images):
    # Crop ảnh con
    left = i * sub_width
    right = (i + 1) * sub_width
    cropped = image.crop((left, 0, right, height))
    
    # Xoay ảnh con 180 độ
    rotated = cropped.rotate(180)
    rotated_images.append(rotated)

# Tạo ảnh mới để ghép các ảnh con lại
final_image = Image.new("RGB", (width, height))

# Ghép các ảnh con vào ảnh mới
for i, rotated in enumerate(rotated_images):
    final_image.paste(rotated, (i * sub_width, 0))

# Lưu ảnh hoàn chỉnh
final_image.save("final_image.png")
final_image.show()
