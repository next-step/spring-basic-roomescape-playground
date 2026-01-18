package roomescape.member;

public record MemberResponseDto(Long id, String name, String email) {
    public static MemberResponseDto from(Member member) {
        return new MemberResponseDto(member.getId(), member.getName(), member.getEmail());
    }
}


