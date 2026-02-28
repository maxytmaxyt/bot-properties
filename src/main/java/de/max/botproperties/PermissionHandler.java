package de.max.permission;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import java.util.Collection;

/**
 * Enhanced permission system for JDA 6.3.0.
 * Supports individual Discord permissions and hierarchy logic.
 */
public class PermissionHandler {

    /**
     * Checks if a member has the Administrator permission.
     * @param member The member to check.
     * @return True if the member is an administrator.
     */
    public boolean isAdministrator(Member member) {
        return member.hasPermission(Permission.ADMINISTRATOR);
    }

    /**
     * Checks if a member can manage the server (guild).
     * @param member The member to check.
     * @return True if the member can manage the server.
     */
    public boolean canManageServer(Member member) {
        return member.hasPermission(Permission.MANAGE_SERVER);
    }

    /**
     * Checks if a member can manage channels.
     * @param member The member to check.
     * @return True if the member can manage channels.
     */
    public boolean canManageChannels(Member member) {
        return member.hasPermission(Permission.MANAGE_CHANNEL);
    }

    /**
     * Checks if a member can manage messages (e.g., for clearing chat).
     * @param member The member to check.
     * @return True if the member can manage messages.
     */
    public boolean canManageMessages(Member member) {
        return member.hasPermission(Permission.MESSAGE_MANAGE);
    }

    /**
     * Checks for moderation capabilities (Kick, Ban, and Moderate Members/Timeout).
     * @param member The member to check.
     * @return True if the member has basic moderation permissions.
     */
    public boolean isModerator(Member member) {
        return member.hasPermission(Permission.KICK_MEMBERS, 
                                   Permission.BAN_MEMBERS, 
                                   Permission.MODERATE_MEMBERS);
    }

    /**
     * Generic check for any specific Discord permission.
     * @param member The member to check.
     * @param perm The permission to verify.
     * @return True if the member has the permission.
     */
    public boolean hasPermission(Member member, Permission perm) {
        return member.hasPermission(perm);
    }

    /**
     * Check if member has a specific role ID.
     * @param member The member to check.
     * @param roleId The Long ID of the role.
     * @return True if the member has the role.
     */
    public boolean hasRole(Member member, Long roleId) {
        return member.getRoles().stream()
                .anyMatch(role -> role.getIdLong() == roleId);
    }

    /**
     * Checks if the issuer is higher in the role hierarchy than the target.
     * @param issuer The member performing an action.
     * @param target The target member.
     * @return True if issuer is ranked higher.
     */
    public boolean canInteract(Member issuer, Member target) {
        /* JDA returns -1 if member has no roles, so we handle hierarchy safely */
        return issuer.getRoles().isEmpty() ? false : 
               target.getRoles().isEmpty() ? true :
               issuer.getRoles().get(0).getPosition() > target.getRoles().get(0).getPosition();
    }
}
