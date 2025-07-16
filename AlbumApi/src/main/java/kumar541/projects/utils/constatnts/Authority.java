package kumar541.projects.utils.constatnts;

public enum Authority {
    
   READ,
   WRITE,
   UPDATE,
   USER,// CAN READ, WRITE,UPDATE,DELETE SELF OBJECTS
   ADMIN // CAN READ, WRITE,UPDATE,DELETE ANY OBJECT
    
}
