🎬 Hệ Thống Đặt Vé Xem Phim – Backend (Spring Boot)
  Backend cho hệ thống đặt vé xem phim trực tuyến: quản lý phim, rạp, suất chiếu, ghế, giá vé, hóa đơn – tích hợp thanh toán VNPay và Chatbot AI (RAG + Tool Calling) hỗ trợ tìm suất chiếu.

✨ Tính năng nổi bậ

👤 Xác thực & Phân quyền:

      JWT Authentication + Role-based Authorization.

      Bộ lọc JwtRequestFilter, JwtTokenUtil, CustomUserDetailsService.

      Mã hóa mật khẩu, validation đầy đủ.

      Hỗ trợ TOTP (2FA-ready).

🎥 Quản lý phim & rạp

  CRUD phim với ngày khởi chiếu / kết thúc / poster / trạng thái.

  Quản lý rạp, chi nhánh, địa chỉ.

  Quản lý phòng chiếu (screen room), loại phòng, layout ghế.

  Quản lý suất chiếu (showtime) theo ngày – giờ – phòng chiếu.

💺 Quy trình đặt ghế hoàn chỉnh

  Flow thực tế:

    Xem phim → chọn suất chiếu

    Chọn ghế → tính giá vé

    Tạo hóa đơn → thanh toán VNPay

    Sinh mã vé (booking code)

Ngăn đặt trùng ghế bằng xử lý giao dịch.

🧾 Hóa đơn & Thống kê doanh thu

  Tạo/lưu hóa đơn theo từng booking.

  Lịch sử giao dịch cho người dùng.

  Module thống kê doanh thu tháng – năm – tổng quan cho admin.

  API cho dashboard: vé theo ngày/tháng, doanh thu, KPI.

💳 Thanh toán VNPay

    Tích hợp VNPay đầy đủ:

      Sinh URL thanh toán

      Kiểm tra checksum

      Xử lý callback

  Trạng thái đơn cập nhật tự động sau thanh toán.

⚡ Hiệu năng & mở rộng:

  Redis làm caching + lưu ChatMemory.

  Tối ưu truy vấn dữ liệu cho showtime & đặt vé.

  Docker hóa toàn bộ hệ thống.

🤖 Chatbot AI (RAG + Tool Calling)

  Implement bằng Spring AI + Gemini API:

    Vector DB (Qdrant) lưu embedding của phim & rạp, phòng chiếu.

    RAG Ingestion: đọc dữ liệu MySQL → chunk → embed → push Qdrant.

    Redis ChatMemory: ghi nhớ hội thoại.

  Tool Calling:

    Chatbot gọi hàm Java để truy vấn suất chiếu thực trong DB.

🧱 Công nghệ sử dụng
| Nhóm       | Công nghệ                           |
| ---------- | ----------------------------------- |
| Ngôn ngữ   | Java                                |
| Framework  | Spring Boot                         |
| Bảo mật    | Spring Security, JWT                |
| Database   | MySQL                               |
| ORM        | Spring Data JPA                     |
| Cache      | Redis                               |
| AI         | Spring AI, Gemini, Qdrant Vector DB |
| Công cụ    | ModelMapper, Lombok                 |
| DevOps     | Docker & Docker Compose             |
| Thanh toán | VNPay                               |

🚀 Hướng dẫn chạy dự án
1) Clone project:
      git clone https://github.com/<your-username>/movie-ticket-booking-service.git

      cd movie-ticket-booking-service

3) Tạo file .env:
    DB_USER=root

    DB_PASS=123456

    MYSQL_DB=movie_ticket_booking

    JWT_SIGNER_KEY=your_secret_key

    GEMINI_KEY=your_gemini_api_key

    VNP_TMN_CODE=...

    VNP_HASH_SECRET=...

    VNP_PAY_URL=...

    VNP_RETURN_URL=...

    QDRANT_HOST=qdrant

    QDRANT_PORT=6333
   
5) Chạy bằng Docker:
      docker compose up -d --build

6) Chạy local
      Chạy service bằng IDE intellij

7) Hình ảnh kết quả đạt được sau khi chạy ứng dụng:

     Thống kê doanh thu theo phim

<img width="1918" height="870" alt="image" src="https://github.com/user-attachments/assets/8048f0dc-ab1c-46d0-960d-0994fed6b885" />

    Thống kê doanh thu theo rạp

<img width="1919" height="867" alt="image" src="https://github.com/user-attachments/assets/a1efff6f-275e-4292-870e-6dabbba1ce62" />

    Quản lý phim

<img width="1919" height="870" alt="image" src="https://github.com/user-attachments/assets/b29c2177-6b75-4ab2-9ef2-2fc4b95d0584" />

    Quản lý rạp

<img width="1919" height="870" alt="image" src="https://github.com/user-attachments/assets/0c70109c-dccb-4be4-8a80-5230cd7941fe" />

    Quản lý phòng chiếu

  <img width="1919" height="868" alt="image" src="https://github.com/user-attachments/assets/c3cb3402-e673-46cb-8efb-7eb1cb03f777" />

    Quản lý loại phòng chiếu

  <img width="1919" height="871" alt="image" src="https://github.com/user-attachments/assets/46278673-2158-4717-a08b-fa7d22ac7cc6" />

    Quản lý Ghế

  <img width="1915" height="870" alt="image" src="https://github.com/user-attachments/assets/f770b631-27b0-4d39-8088-c613599255e5" />

    Quản lý suất chiếu

  <img width="1919" height="866" alt="image" src="https://github.com/user-attachments/assets/476c1a90-11ef-4968-822f-d96142fd9d78" />

    Quản lý giá vé

  <img width="1919" height="867" alt="image" src="https://github.com/user-attachments/assets/de6bfada-9d93-4d33-9fac-60a462bba15b" />

    Quản lý đơn hàng

  <img width="1919" height="868" alt="image" src="https://github.com/user-attachments/assets/210fe286-a84c-4bbf-b9e3-f4e59d0b5c93" />

    Chatbot

  <img width="1919" height="872" alt="image" src="https://github.com/user-attachments/assets/01d90498-3921-4877-9ae9-4f7e34965d19" />

  <img width="1918" height="870" alt="image" src="https://github.com/user-attachments/assets/414031d3-a979-4ab0-8f6f-c27ece4708f2" />

    Thanh toán

  <img width="1919" height="870" alt="image" src="https://github.com/user-attachments/assets/9d57df81-2c5a-4d1b-a79c-acf92cc92ddc" />
