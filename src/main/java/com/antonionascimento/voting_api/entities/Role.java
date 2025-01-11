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
        
        ADMIN(1l),
        USER(2l);

        private final long roleId;
         
        roleType(long roleId){
            this.roleId = roleId;
        }

        public long getRoleId() {
            return roleId;
        }

    }

}
