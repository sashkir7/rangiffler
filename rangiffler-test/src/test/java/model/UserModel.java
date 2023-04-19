package model;

import helper.DataHelper;
import lombok.Builder;
import lombok.Data;
import sashkir7.grpc.Photo;
import sashkir7.grpc.User;

import java.util.*;

@Data
@Builder
public final class UserModel {

    private String username, password, firstname, lastname;
    private String avatarImageClasspath;

    @Builder.Default
    private Set<Photo> photos = new HashSet<>();

    @Builder.Default
    private Map<PartnerStatus, List<UserModel>> partners = new HashMap<>();

    public List<UserModel> getFriends() {
        return partners.get(PartnerStatus.FRIEND);
    }

    public void addPhoto(Photo photo) {
        photos.add(photo);
    }

    public void addPartner(PartnerStatus status, UserModel partner) {
        partners.computeIfAbsent(status, k -> new ArrayList<>());
        partners.get(status).add(partner);
    }

    public User toGrpc() {
        User.Builder builder = User.newBuilder()
                .setUsername(username)
                .setFirstname(firstname)
                .setLastname(lastname);
        if (avatarImageClasspath != null && !avatarImageClasspath.isBlank())
            builder.setAvatar(DataHelper.imageByClasspath(avatarImageClasspath));
        return builder.build();
    }

}
