🎬 Hệ Thống Đặt Vé Xem Phim – Backend (Spring Boot)
  Backend cho hệ thống đặt vé xem phim trực tuyến: quản lý phim, rạp, suất chiếu, ghế, giá vé, hóa đơn – tích hợp thanh toán VNPay và Chatbot AI (RAG + Tool Calling) hỗ trợ tìm suất chiếu.
✨ Tính năng nổi bật
👤 Xác thực & Phân quyền

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

    Vector DB (Qdrant) lưu embedding của phim & rạp.

    RAG Ingestion: đọc dữ liệu MySQL → chunk → embed → push Qdrant.

    Redis ChatMemory: ghi nhớ hội thoại.

  Tool Calling:

    Chatbot gọi hàm Java để truy vấn suất chiếu thực trong DB.

  Chatbot có thể trả lời:

  "Chiếu phim abc ở xyz ngày dd/MM/yyyy (hôm nay, ngày mai,..) lúc mấy giờ?"

🧱 Công nghệ sử dụng
| Nhóm       | Công nghệ                           |
| ---------- | ----------------------------------- |
| Ngôn ngữ   | Java 21                            |
| Framework  | Spring Boot 3.x                     |
| Bảo mật    | Spring Security, JWT                |
| Database   | MySQL 8.x                           |
| ORM        | Spring Data JPA                     |
| Cache      | Redis                               |
| AI         | Spring AI, Gemini, Qdrant Vector DB |
| Công cụ    | ModelMapper, Lombok                 |
| DevOps     | Docker & Docker Compose             |
| Thanh toán | VNPay                               |

🚀 Hướng dẫn chạy dự án
1) Clone project
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
   
4) Chạy bằng Docker
  docker compose up -d --build

5) Chạy local
  Chạy service bằng IDE intellij

  
