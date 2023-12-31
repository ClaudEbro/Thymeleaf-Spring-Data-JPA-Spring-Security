package ca.ebrottie.hospitalmgmt.web;


import ca.ebrottie.hospitalmgmt.entities.Patient;
import ca.ebrottie.hospitalmgmt.repository.PatientRepository;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
* @author eclaude
 * */


@Controller
@AllArgsConstructor
public class PatientController {

    private PatientRepository patientRepository; // Injection by constructor

    @GetMapping("/user/index")
    public String index(Model model,
                        @RequestParam(name="page", defaultValue ="0")int p,
                        @RequestParam(name="size", defaultValue ="4") int s,
                        @RequestParam(name="keyword", defaultValue ="") String kw){
        Page<Patient> pagePatients = patientRepository.findByNameContains(kw, PageRequest.of(p,s)); // I call patients from database and store them in a model.
        model.addAttribute("listPatients", pagePatients.getContent());
        model.addAttribute("pages",new int[pagePatients.getTotalPages()]);
        model.addAttribute("currentPage", p);
        model.addAttribute("keyword", kw);
        return "patients";
    }

    @GetMapping("/admin/deletePatient")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String delete(Long id, String keyword, int page){
        patientRepository.deleteById(id);
        return "redirect:/user/index?page="+page+"&keyword="+keyword;
    }

    @GetMapping("/")
    public String home(){
        return "redirect:/user/index";
    }

    @GetMapping("/admin/formPatients")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String formPatient(Model model){
        model.addAttribute("patient", new Patient());
        return "formPatients";
    }

    @PostMapping("/admin/savePatient")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String savePatient(@Valid Patient patient, BindingResult bindingResult){

        if(bindingResult.hasErrors()){
            return "formPatients";
        }
        patientRepository.save(patient);
        return "redirect:/user/index?keyword="+patient.getName();
    }

    @GetMapping("/admin/editPatient")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String editPatient(Model model, @RequestParam(name="id") Long id){
        Patient patient = patientRepository.findById(id).get();
        model.addAttribute("patient", patient);
        return "editPatient";
    }

}
