package com.team1.sgart.backend.http;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.team1.sgart.backend.model.UserDTO;
import com.team1.sgart.backend.model.Admin;
import com.team1.sgart.backend.model.AdminDTO;
import com.team1.sgart.backend.services.AdminService;

@RestController
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private AdminService adminService;
    
    
    @GetMapping("/getUsuariosValidados")
    public ResponseEntity<List<UserDTO>> getUsuariosValidados(){
    	List<UserDTO> users=adminService.getUsuariosValidados();
    	return ResponseEntity.ok(users);
    }
    
    @PostMapping("/modificarAdmin")
	public ResponseEntity<String> modificarAdmin(@RequestBody Admin admin) {
		adminService.modificarAdmin(admin);
		return ResponseEntity.status(HttpStatus.OK).body("Perfil modificado correctamente");
	}
    
    @PostMapping("/crearAdmin")
	public ResponseEntity<String> crearAdmin(@RequestBody Admin admin) {
		UUID id=adminService.crearAdmin(admin);
		return ResponseEntity.ok(id.toString());
	}

    @GetMapping("/getAdmins")
    public ResponseEntity<List<AdminDTO>> getAdmins(){
    	List<AdminDTO> admins=adminService.getAdmins();
    	return ResponseEntity.ok(admins);
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

    @DeleteMapping("/eliminar/email/{email}")
    public ResponseEntity<String> eliminarUsuarioPorEmail(@PathVariable ("email") String email) {
        adminService.eliminarUsuarioPorEmail(email);
        return new ResponseEntity<>("Perfil eliminado con éxito", HttpStatus.OK);
    }
    
    @PostMapping("/verificarEmail")
	public ResponseEntity<String> verificarEmail(@RequestBody String email) {
		boolean existe = adminService.emailAdminEstaRegistrado(email);
		if (!existe) {
			return ResponseEntity.status(HttpStatus.OK).body("El email no está registrado");
		} else {
			return ResponseEntity.status(HttpStatus.CONFLICT).body("El email está registrado");
		}
	}
}

