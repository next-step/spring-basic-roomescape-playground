package roomescape.member;

public class MemberResponse {

    public static class Create {
        private Long id;
        private String name;
        private String email;

        public Create(Long id, String name, String email) {
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

    public static class Check {
        private String name;

        public Check(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        public static Check from(Member member) {
            return new Check(member.getName());
        }
    }

}
