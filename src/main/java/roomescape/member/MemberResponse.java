package roomescape.member;

public class MemberResponse {

    public static class MemberInfoResponse{
        private Long id;
        private String name;
        private String email;

        public MemberInfoResponse(Long id, String name, String email) {
            this.id = id;
            this.name = name;
            this.email = email;
        }

        public Long getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public String getEmail() {
            return email;
        }
    }

    public static class AuthorizationResponse{
        private String name;
        private String role;

        public AuthorizationResponse(String name, String role) {
            this.name = name;
            this.role = role;
        }

        public String getName() {
            return name;
        }

        public String getRole(){
            return role;
        }
    }
}
