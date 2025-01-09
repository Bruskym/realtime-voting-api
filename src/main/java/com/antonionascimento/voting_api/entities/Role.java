package com.antonionascimento.voting_api.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "tb_roles")
public class Role {
    
    @Id
    private long id;

    @Column(nullable = false, unique = true)
    private String name;

    public enum roleType{
        
        ADMIN(1l, "ADMIN"),
        USER(2l, "USER");

        private final long roleId;
        private final String name;
         
        roleType(long roleId, String name){
            this.roleId = roleId;
            this.name = name;
        }

        public long getRoleId() {
            return roleId;
        }

        public String getName() {
            return name;
        }
    }

}
