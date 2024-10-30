package com.team1.sgart.backend.http;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.team1.sgart.backend.model.UserDTO;
import com.team1.sgart.backend.services.AdminService;

@RestController
@RequestMapping("/admin")
@CrossOrigin("*")
public class AdminController {

    @Autowired
    private AdminService adminService;
    
    
    @GetMapping("/getUsuariosValidados")
    public ResponseEntity<List<UserDTO>> getUsuariosValidados(){
    	List<UserDTO> users=adminService.getUsuariosValidados();
    	return ResponseEntity.ok(users);
    }
    
    @GetMapping("/getUsuariosSinValidar")
    public ResponseEntity<List<UserDTO>> getUsuariosSinValidar(){
    	List<UserDTO> users=adminService.getUsuariosSinValidar();
    	return ResponseEntity.ok(users);
    }

    @PutMapping("/validar/{email}")
    public ResponseEntity<String> validarUsuario(@PathVariable String email) {
        adminService.validarUsuario(email);
        return new ResponseEntity<>("Usuario validado con éxito", HttpStatus.OK);
    }
    
    @PutMapping("/cambiarHabilitacion/{email}")
    public ResponseEntity<String> cambiarHabilitacionUsuario(@PathVariable String email){
    	try {
    		adminService.cambiarHabilitacionUsuario(email);
    	}catch(IllegalArgumentException e) {
    		return new ResponseEntity<>("No se pudo realizar el cambio.", HttpStatus.BAD_REQUEST);
    	}
    	return new ResponseEntity<>("Usuario cambiado con éxito", HttpStatus.OK);
    }
    
    @DeleteMapping("/eliminar/id/{id}")
    public ResponseEntity<String> eliminarUsuarioPorId(@PathVariable Integer id) {
        adminService.eliminarUsuarioPorId(id);
        return new ResponseEntity<>("Usuario eliminado con éxito", HttpStatus.OK);
    }

    @DeleteMapping("/eliminar/email/{email}")
    public ResponseEntity<String> eliminarUsuarioPorEmail(@PathVariable String email) {
        adminService.eliminarUsuarioPorEmail(email);
        return new ResponseEntity<>("Usuario eliminado con éxito", HttpStatus.OK);
    }
}

