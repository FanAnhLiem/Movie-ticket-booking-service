# 🎬 Movie Ticket Booking System – Backend (Spring Boot)

Backend cho hệ thống **đặt vé xem phim trực tuyến** với đầy đủ nghiệp vụ thực tế:
- Quản lý phim, rạp, phòng chiếu, ghế, suất chiếu, giá vé
- Quy trình đặt vé – thanh toán – sinh mã vé
- Thống kê doanh thu cho admin
- Tích hợp **VNPay**
- Tích hợp **Chatbot AI (RAG + Tool Calling)** hỗ trợ tìm suất chiếu

---

## ✨ Tính năng nổi bật

### 👤 Xác thực & Phân quyền
- JWT Authentication + Role-based Authorization
- Custom `JwtRequestFilter`, `JwtTokenUtil`, `CustomUserDetailsService`
- Mã hóa mật khẩu, validation request đầy đủ
- Hỗ trợ **TOTP (2FA-ready)**

---

### 🎥 Quản lý Phim & Rạp
- CRUD phim:
  - Ngày khởi chiếu / kết thúc
  - Poster
  - Trạng thái
- Quản lý:
  - Rạp
  - Chi nhánh
  - Địa chỉ
- Quản lý:
  - Phòng chiếu
  - Loại phòng
  - Layout ghế
- Quản lý suất chiếu theo:
  - Phim
  - Ngày – giờ
  - Phòng chiếu

---

### 💺 Quy trình Đặt ghế hoàn chỉnh
**Flow nghiệp vụ thực tế:**

1. Xem phim → chọn suất chiếu  
2. Chọn ghế → tính giá vé  
3. Tạo hóa đơn  
4. Thanh toán VNPay  
5. Sinh mã vé (booking code)

✅ Ngăn chặn **đặt trùng ghế** bằng xử lý transaction & locking.

---

### 🧾 Hóa đơn & Thống kê doanh thu
- Lưu hóa đơn theo từng booking
- Lịch sử giao dịch theo user
- Thống kê:
  - Doanh thu theo ngày
  - Theo tháng
  - Tổng quan
- API riêng phục vụ **Admin Dashboard**
- KPI: vé bán ra, doanh thu, phim hot

---

### 💳 Thanh toán VNPay
- Sinh URL thanh toán
- Kiểm tra checksum
- Xử lý callback
- Cập nhật trạng thái đơn tự động sau thanh toán

---

### ⚡ Hiệu năng & Mở rộng
- Redis:
  - Cache dữ liệu
  - Lưu **Chat Memory**
- Tối ưu query cho:
  - Showtime
  - Booking
- Docker hóa toàn hệ thống

---

## 🤖 Chatbot AI (RAG + Tool Calling)

### 🧠 Công nghệ:
- **Spring AI + Gemini API**
- **Vector DB: Qdrant**
- **Redis ChatMemory**

### 🔍 Cơ chế RAG:
1. Lấy dữ liệu từ MySQL (phim, rạp, phòng)
2. Chunk dữ liệu
3. Tạo embedding
4. Đẩy lên Qdrant

### 🛠 Tool Calling:
- Chatbot có thể gọi trực tiếp **hàm Java**
- Truy vấn **suất chiếu thực tế trong DB**
- Trả lời đúng dữ liệu realtime

---

## 🧱 Công nghệ sử dụng

| Nhóm       | Công nghệ |
|------------|-----------|
| Ngôn ngữ   | Java |
| Framework  | Spring Boot |
| Bảo mật    | Spring Security, JWT |
| Database   | MySQL |
| ORM        | Spring Data JPA |
| Cache      | Redis |
| AI         | Spring AI, Gemini, Qdrant |
| Dev Tools  | Lombok, ModelMapper |
| DevOps     | Docker, Docker Compose |
| Thanh toán | VNPay |

---

## 🚀 HƯỚNG DẪN CHẠY DỰ ÁN

### 1️⃣ Clone project
```bash
git clone https://github.com/<your-username>/movie-ticket-booking-service.git
cd movie-ticket-booking-service
